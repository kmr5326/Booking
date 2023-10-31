package com.ssafy.booking.viewmodel

import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.data.remote.model.ChatRequest
import com.ssafy.data.remote.model.KafkaMessage
import com.ssafy.domain.usecase.ConnectSocketService
import com.ssafy.domain.usecase.HandShakingService
import com.ssafy.domain.usecase.SendMessageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import java.time.LocalDateTime.now
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val handShakingService: HandShakingService,
    private val connectSocketService: ConnectSocketService,
    private val sendMessageService: SendMessageService,
) : ViewModel() {

    private val handShakeRequest = Request.Builder()
        .url("wss://k9c206.p.ssafy.io:10001/booking/chat")
        .build()

    private val handShakeListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d("CHAT", "HandShake Success $response $webSocket")
            val test: ChatRequest = ChatRequest(KafkaMessage("test", "1", now()), "1")
            webSocket.send(test.toString())
            Log.d("CHAT", "${test.toString()} send")
        }
        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.d("CHAT", "HandShake Fail $webSocket $t $response")
        }
        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("CHAT", "Received message: $text")
            val jsonResponse = JSONObject(text)
            if (jsonResponse.optString("status") == "success") {
                Log.d("CHAT", "Message sent successfully!")
            }
        }
    }

    fun handShakingService() {
        val client = HandShakingService.OkHttpClientSingleton.provideOkHttpClient()
        val webSocket = client.newWebSocket(handShakeRequest, handShakeListener)
        Log.d("CHAT", "client ${client.toString()}")
        Log.d("CHAT", "webSocket ${webSocket.toString()}")
    }

    private val subscribeRequest = Request.Builder()
        .url("wss://k9c206.p.ssafy.io:10001/subscribe/1")
        .header("Upgrade", "websocket")
        .build()

    private val subscribeListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d("CHAT", "onOpen $response $webSocket")

        }
        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.d("CHAT", "onFailure $webSocket $t $response")
        }
    }

    fun SubscribeService() {
        val client = HandShakingService.OkHttpClientSingleton.provideOkHttpClient()
        val webSocket = client.newWebSocket(subscribeRequest, subscribeListener)
        Log.d("CHAT", "client ${client.toString()}")
        Log.d("CHAT", "webSocket ${webSocket.toString()}")
    }


    fun connectSocket() {
        viewModelScope.launch {
            connectSocketService.execute()
        }
    }

    fun sendMessage(text: TextFieldValue) {
        viewModelScope.launch {
            sendMessageService.execute(text)
        }
    }

}