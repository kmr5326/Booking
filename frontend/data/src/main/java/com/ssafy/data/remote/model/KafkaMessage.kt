package com.ssafy.data.remote.model

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
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