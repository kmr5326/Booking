package com.ssafy.domain.model

import com.google.gson.annotations.SerializedName

data class ChatRoom (
    @SerializedName("chatroomId")
    val chatroomId: Int,
    @SerializedName("meetingTitle")
    val meetingTitle : String,
    @SerializedName("memberList")
    val memberList: List<Int>
)