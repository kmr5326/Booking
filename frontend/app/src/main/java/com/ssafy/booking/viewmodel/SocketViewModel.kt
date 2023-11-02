package com.ssafy.booking.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.StompClient
import com.google.gson.GsonBuilder
import com.ssafy.domain.model.KafkaMessage
import com.ssafy.data.utils.LocalDateTimeDeserializer
import com.ssafy.data.utils.LocalDateTimeSerializer
import com.ssafy.domain.usecase.OkhttpService
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import java.time.LocalDateTime
import java.util.Scanner
import java.util.logging.Logger
import javax.inject.Inject
@HiltViewModel
class SocketViewModel @Inject constructor() : ViewModel() {
    val logger = Logger.getLogger("CHAT")

    var stompConnection: Disposable
    lateinit var topic: Disposable

    private val url2 = "wss://k9c206.p.ssafy.io:10001/booking/chat"
    private val intervalMillis = 1000L
    private val client = OkhttpService.OkHttpClientSingleton.provideOkHttpClient()

    val stomp = StompClient(client, intervalMillis).apply { this@apply.url = url2 }

    fun sendMessage(message: KafkaMessage) {
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer())
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer())
            .create()
        val jsonMessage = gson.toJson(message)

        stomp.send("/publish/message/1", jsonMessage)
            .subscribe { success ->
                if (success) {
                    Log.d("CHAT", "chatting send is successful ${jsonMessage}")
                } else {
                    Log.d("CHAT", "failed to send message")
                }
            }
    }

    init {
        stompConnection = stomp.connect().subscribe {
            when (it.type) {
                Event.Type.OPENED -> {

                    // subscribe
                    topic = stomp.join("/subscribe/1")
                        .subscribe { Log.d("CHAT", it) }

                    // unsubscribe
//                topic.dispose()

//                val test = KafkaMessage("Hello 희창", 1, now())

                }

                Event.Type.CLOSED -> {

                }

                Event.Type.ERROR -> {

                }

                else -> {
                    Log.d("CHAT", "else")
                }

            }
        }

        val scanner = Scanner(System.`in`)
//        scanner.nextLine()

//    // disconnect
//    stompConnection.dispose()
    }
}
