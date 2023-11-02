package com.ssafy.domain.model
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class KafkaMessage (
    @SerializedName("message")
    val message: String,

    @SerializedName("senderId")
    val senderId: Long , // sender -> 보내는 유저 PK값

    @SerializedName("sendTime")
    val sendTime: LocalDateTime) {
}