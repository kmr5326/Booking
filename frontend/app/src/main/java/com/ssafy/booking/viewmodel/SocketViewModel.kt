package com.ssafy.booking.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.StompClient
import com.google.gson.GsonBuilder
import com.ssafy.data.room.dao.MessageDao
import com.ssafy.data.room.entity.MessageEntity
import com.ssafy.domain.model.KafkaMessage
import com.ssafy.data.utils.LocalDateTimeDeserializer
import com.ssafy.data.utils.LocalDateTimeSerializer
import com.ssafy.domain.usecase.OkhttpService
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.logging.Logger
import javax.inject.Inject

@HiltViewModel
class SocketViewModel @Inject constructor(
    private val messageDao: MessageDao
) : ViewModel() {

    val logger = Logger.getLogger("STOMP")

    lateinit var stompConnection: Disposable
    lateinit var topic: Disposable
    private val bookingwss = "wss://k9c206.p.ssafy.io:10001/booking/chat"
    private val intervalMillis = 1000L
    private val client = OkhttpService.OkHttpClientSingleton.provideOkHttpClient()
    val stomp = StompClient(client, intervalMillis).apply { this@apply.url = bookingwss }
    val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer())
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer())
        .create()

    var messages: LiveData<List<MessageEntity>> = MutableLiveData(emptyList())

    fun loadMessages(chatId: String) {
        viewModelScope.launch {
            messages = messageDao.getAll(chatId.toInt())
        }
    }

    fun connectToChat(chatId: String) {
        stompConnection = stomp.connect().subscribe {
            when (it.type) {
                Event.Type.OPENED -> {
                    Log.d("STOMP", "${it} OPENED!!!")
                    topic = stomp.join("/subscribe/$chatId")
                        .subscribe(
                            { stompMessage ->
                                // Get Message
                                Log.d("STOMP", "Received: $stompMessage")
                                // Parse KafkaMessage
                                val kafkaMessage: KafkaMessage =
                                    gson.fromJson(stompMessage, KafkaMessage::class.java)
                                Log.d("STOMP", "Parsed Message: $kafkaMessage")
                                // to MessageEntity
                                val messageEntity = MessageEntity(
                                    chatId = chatId.toInt(),
                                    senderId = kafkaMessage.senderId,
                                    sendTime = kafkaMessage.sendTime,
                                    content = kafkaMessage.message,
                                    senderName = kafkaMessage.senderName
                                )
                                Log.d("STOMP", "Converted to Entity: $messageEntity")
                                // Insert Room DB
                                viewModelScope.launch(Dispatchers.IO) {
                                    try {
                                        messageDao.insert(messageEntity)
                                        loadMessages(chatId)
                                    } catch (e: Exception) {
                                        Log.e("STOMP", "Error inserting message into database", e)
                                    }
                                }
                            },
                            { throwable ->
                                Log.e("STOMP", "Error :", throwable)
                            }
                        )
                }
                Event.Type.CLOSED -> { Log.d("STOMP", "${it} CLOSED!!!") }
                Event.Type.ERROR -> { Log.d("STOMP", "${it} ERROR!!!") }
                else -> { Log.d("STOMP", "else") }
            }
        }
    }

    fun sendMessage(message: KafkaMessage, chatId: Long?) {
        val jsonMessage = gson.toJson(message)
        stomp.send("/publish/message/${chatId}", jsonMessage)
            .subscribe { success ->
                if (success) {
                    Log.d("STOMP", "chatting send is successful ${jsonMessage}")
                } else {
                    Log.d("STOMP", "failed to send message")
                }
            }
    }

    fun disconnectChat() {
        topic.dispose()  // 구독 해지
        stompConnection.dispose()  // STOMP 연결 해지
    }

}
