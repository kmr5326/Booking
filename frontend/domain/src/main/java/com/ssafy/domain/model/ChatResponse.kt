package com.ssafy.domain.model

import com.google.gson.annotations.SerializedName

data class ChatRoom (
    @SerializedName("chatroomId")
    val chatroomId: Int,
    @SerializedName("lastMessageIdx")
    val lastMessageIdx: Int,
    @SerializedName("meetingTitle")
    val meetingTitle : String,
    @SerializedName("lastMessage")
    val lastMessage : String?,
    @SerializedName("memberList")
    val memberList: List<Int>
)