package com.ssafy.booking.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.isActive
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

    private val _finalMessages = MutableLiveData<List<MessageEntity>>()
    val finalMessages: LiveData<List<MessageEntity>> get() = _finalMessages

    private var allMessagesSource: LiveData<List<MessageEntity>>? = null
    private var latestMessagesSource: LiveData<List<MessageEntity>>? = null

    // 전체 메시지 불러오기
    fun loadAllMessage(chatId: Int) {
        allMessagesSource = messageDao.getAllMessage(chatId).also {
            it.observeForever { allMessages ->
                _finalMessages.postValue(allMessages)
            }
            Log.d("CHAT_DETAIL", "전체 메시지 갱신")
        }
    }
    
    // 로컬에 있지만 불러오지 않은 메시지 불러오기
    fun loadLatestMessage(chatId: Int) {
        viewModelScope.launch {
            messageDao.getUnusedMessage(chatId).asFlow()
                .distinctUntilChanged()  // 중복 데이터 제거
                .collect { newMessages ->
                    val currentMessages = _finalMessages.value.orEmpty()
                    val uniqueNewMessages = newMessages.filterNot { it.id in currentMessages.map { msg -> msg.id } } // 증복 등장 방지
                    _finalMessages.postValue(currentMessages + uniqueNewMessages) // 새 메시지 추가

                    uniqueNewMessages.forEach { newMessage ->
                        messageDao.markUsedMessage(newMessage.id) // 읽음 처리
                    }
                }
        }
    }
    private val _setPollingMessage = MutableLiveData<Boolean>(false)
    val setPollingMessage: LiveData<Boolean> get() = _setPollingMessage
    private var PollingJob: Job? = null
    fun starMessagePolling(chatId: Int) {
        PollingJob = viewModelScope.launch(Dispatchers.Main) {
            while (isActive) {
                loadLatestMessage(chatId)
                Log.d("CHAT_DETAIL", "목록 갱신")
                delay(500)
            }
        }
    }
    fun stopMessagePolling() {
        Log.d("CHAT_DETAIL", "갱신 중지")
        PollingJob?.cancel()
    }
    fun setPollingMessage(chatId: Int, toggle: Boolean) {
        _setPollingMessage.value = toggle
        if (toggle) {
            starMessagePolling(chatId)
        } else {
            stopMessagePolling()
        }
    }

    // Observer 메모리 누수 방지
    override fun onCleared() {
//        Log.d("CHAT_DETAIL", "onCleared")
        super.onCleared()
        allMessagesSource?.let { it.removeObserver {} }
        latestMessagesSource?.let { it.removeObserver {} }
        finalMessages?.let { it.removeObserver{} }
    }

    fun postLastReadMessageId(chatroomId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            val lastReadId = chatDao.getLastReadMessageId(chatroomId) ?: 1
            Log.d("CHAT_DETAIL", "마지막으로 읽은 메시지 보내기 ${lastReadId}")
            val lastMessageRequest = LastReadMessageRequest(lastMessageIndex = lastReadId)
            try {
                val messageResponses = chatUseCase.postLastReadMessage(chatroomId, lastMessageRequest)
                val lastReadMessageIdx = messageResponses.maxOfOrNull { it.messageId } ?: 0
                val chatEntity = ChatEntity(chatroomId, lastReadMessageIdx)
                chatDao.updateLastReadMessage(chatEntity)
                Log.d("CHAT_DETAIL", "마지막으로 읽은 메시지 업데이트 ${chatEntity}")

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
                // 메시지를 Room에 저장
                messageEntities.forEach { messageEntity ->
                    val existingEntity = messageDao.getMessageByChatIdAndMessageId(
                        messageEntity.chatroomId,
                        messageEntity.messageId
                    )
                    if (existingEntity == null) {
                        messageDao.insertMessage(messageEntity)
                        Log.d("CHAT_DETAIL", "신규 메시지 룸 저장 ${existingEntity}")
                    } else if(existingEntity.readCount != messageEntity.readCount) {
                        existingEntity.messageId?.let {
                            existingEntity.readCount?.let { cnt ->
                                messageDao.updateReadCount(it, cnt)
                                Log.d("CHAT_DETAIL", "읽음 처리 ${existingEntity}")
                            }
                        }
                    } else {
                        Log.d("CHAT_DETAIL", "중복된 메시지가 처리되었습니다.")
                    }
                }

            } catch (e: Exception) {
                Log.e("CHAT_DETAIL", "postLastRead $e")
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
                        Log.d("STOMP", "마지막으로 읽은 메시지 보내기 + 최신 메시지 받기")
                                postLastReadMessageId(chatId.toInt())
                                // delay(1000)
                        Log.d("STOMP", "최신 메시지 갱신")
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
