package com.ssafy.data.remote.api

import com.ssafy.domain.model.ChatRoom
import retrofit2.http.GET

interface ChatListApi {
    @GET("/api/chat/room/list")
    suspend fun getChatList() : List<ChatRoom>
}