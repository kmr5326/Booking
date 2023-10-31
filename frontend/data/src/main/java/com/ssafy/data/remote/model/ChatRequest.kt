package com.ssafy.data.remote.model

import androidx.compose.ui.text.input.TextFieldValue
import com.google.gson.annotations.SerializedName

data class ChatRequest (

    val kafkaMessage: KafkaMessage,
    val chatroomId: String
)
