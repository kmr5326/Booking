package com.ssafy.data.remote.model

import java.time.LocalDateTime

data class KafkaMessage (
    val message: String,
    val sender: String , // sender -> 보내는 유저 PK값
    val sendTime: LocalDateTime) {

}