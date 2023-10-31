package com.ssafy.domain.usecase

import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

class HandShakingService @Inject constructor() {

    @Module
    @InstallIn(SingletonComponent::class)
    object OkHttpClientSingleton {
        @Provides
        @Singleton
        fun provideOkHttpClient() : OkHttpClient =
            OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .pingInterval(30, TimeUnit.SECONDS)
                .build()
    }

}

class ConnectSocketService @Inject constructor() {
    fun execute() {

    }
}
class SendMessageService @Inject constructor() {
    fun execute(message: TextFieldValue) {
        Log.d("CHAT", message.text)
    }
}


