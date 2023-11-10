package com.ssafy.booking.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.StompClient
import com.google.gson.GsonBuilder
import com.ssafy.booking.di.App
import com.ssafy.data.room.dao.ChatDao
import com.ssafy.data.room.dao.MessageDao
import com.ssafy.data.room.entity.ChatEntity
import com.ssafy.data.room.entity.MessageEntity
import com.ssafy.data.utils.LocalDateTimeDeserializer
import com.ssafy.data.utils.LocalDateTimeSerializer
import com.ssafy.domain.model.KafkaMessage
import com.ssafy.domain.model.LastReadMessageRequest
import com.ssafy.domain.repository.ChatRepository
import com.ssafy.domain.usecase.ChatUseCase
import com.ssafy.domain.usecase.OkhttpService
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.logging.Logger
import javax.inject.Inject

@HiltViewModel
class SocketViewModel @Inject constructor(
    private val messageDao: MessageDao,
    private val chatDao: ChatDao,
    private val chatUseCase: ChatUseCase
) : ViewModel() {

    val logger = Logger.getLogger("STOMP")

    lateinit var stompConnection: Disposable
    lateinit var topic: Disposable
    private val bookingwss = "wss://k9c206.p.ssafy.io:10001/booking/chat"
    private val intervalMillis = 1000L
    private val client = OkhttpService.OkHttpClientSingleton.provideOkHttpClient()
    val stomp = StompClient(client, intervalMillis).apply { this@apply.url = bookingwss }
    val gson =
        GsonBuilder().registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer())
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer()).create()


    private val _messages = MutableLiveData<List<MessageEntity>>()
    val messages: LiveData<List<MessageEntity>> get() = _messages

//    fun loadMessages(chatId: Int) {
//        messageDao.getLatestMessage(chatId).observeForever { messageList ->
//            _messages.postValue(messageList)
//        }
//    }

    fun loadLatestMessages(chatId: String) {
        viewModelScope.launch {
            val latestMessagesLiveData = messageDao.getLatestMessage(chatId.toInt())
            latestMessagesLiveData.observeForever { latestMessages ->
                _messages.postValue(latestMessages)
            }
        }
    }

    fun postLastReadMessageId(chatroomId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            val lastReadId = chatDao.getLastReadMessageId(chatroomId) ?: 1
            Log.d("CHAT_DETAIL", "lastReadId ${lastReadId}")
            val lastMessageRequest = LastReadMessageRequest(lastMessageIndex = lastReadId)
            try {
                Log.d("CHAT_DETAIL", "lastMessageRequest ${lastMessageRequest}")
                val messageResponses =
                    chatUseCase.postLastReadMessage(chatroomId, lastMessageRequest)
                Log.d("CHAT_DETAIL", "messageResponses ${messageResponses}")
                val lastReadMessageIdx = messageResponses.maxOfOrNull { it.messageId } ?: 0
                val chatEntity = ChatEntity(chatroomId, lastReadMessageIdx)
                // 마지막으로 읽은 메시지 갱신
                Log.d("CHAT_DETAIL", "chatEntity ${chatEntity}")
                chatDao.updateLastReadMessage(chatEntity)

                val messageEntities = messageResponses.map { response ->
                    MessageEntity(
                        chatroomId = response.chatroomId,
                        messageId = response.messageId,
                        senderId = response.senderId,
                        content = response.content,
                        readCount = response.readCount,
                        timeStamp = response.timestamp
                    )
                }
                Log.d("CHAT_DETAIL", "messageEntities ${messageEntities}")
                // 메시지 저장
                messageEntities.forEach { messageEntity ->
                    val existingEntity = messageDao.getMessageByChatIdAndMessageId(
                        messageEntity.chatroomId,
                        messageEntity.messageId
                    )
                    if (existingEntity == null || existingEntity.readCount != messageEntity.readCount) {
                        messageDao.insertMessage(messageEntity)
                    } else {
                        Log.d("CHAT_DETAIL", "중복된 메시지가 처리되었습니다.")
                    }
                }
            } catch (e: Exception) {
                Log.e("CHAT_DETAIL", "받아오기 $e")
            }
        }


    fun connectToChat(chatId: String) {
        stompConnection = stomp.connect().subscribe {
            when (it.type) {
                Event.Type.OPENED -> {
                    Log.d("STOMP", "$it OPENED!!!")
                    topic = stomp.join("/subscribe/$chatId").subscribe({ stompMessage ->
                        // Get Message
                        Log.d("STOMP", "Received: $stompMessage")
                        // Parse KafkaMessage
                        val kafkaMessage: KafkaMessage =
                            gson.fromJson(stompMessage, KafkaMessage::class.java)
                        Log.d("STOMP", "Parsed Message: $kafkaMessage")
                        viewModelScope.launch(Dispatchers.Main) {
                            try {
                                Log.d("STOMP", "messages ${messages.value}")
                                postLastReadMessageId(chatId.toInt())
                                loadLatestMessages(chatId)
//                                        messageDao.insert(messageEntity)
//                                        loadLatestMessages(chatId)
                            } catch (e: Exception) {
                                Log.e("STOMP", "Error inserting message into database", e)
                            }
                        }
                    }, { throwable ->
                        Log.e("STOMP", "Error :", throwable)
                    })
                }

                Event.Type.CLOSED -> {
                    Log.d("STOMP", "$it CLOSED!!!")
                }

                Event.Type.ERROR -> {
                    Log.d("STOMP", "$it ERROR!!!")
                }

                else -> {
                    Log.d("STOMP", "else")
                }
            }
        }
    }

    fun sendMessage(message: KafkaMessage, chatId: Long?) {
        val jsonMessage = gson.toJson(message)
        stomp.send("/publish/message/$chatId", jsonMessage).subscribe { success ->
            if (success) {
                Log.d("STOMP", "chatting send is successful $jsonMessage")
            } else {
                Log.d("STOMP", "failed to send message")
            }
        }
    }

    fun disconnectChat(chatId: String) {
        viewModelScope.launch {
            Log.d("DISCONNECT", "${chatId} 나가기")
            topic.dispose() // 구독 해지
            stompConnection.dispose() // STOMP 연결 해지
            chatUseCase.deleteDisconnectSocket(chatId.toInt())
        }
    }
}
