package com.ssafy.domain.model

import com.google.gson.annotations.SerializedName

data class ChatCreateRequest (
    @SerializedName("meetingId")
    val meetingId : Int,
    @SerializedName("leaderId")
    val leaderId : Int,
    @SerializedName("meetingTitle")
    val meetingTitle : String,
)