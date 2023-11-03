package com.ssafy.data.remote.api

import com.ssafy.domain.model.ChatCreateRequest
import com.ssafy.domain.model.ChatExitRequest
import com.ssafy.domain.model.ChatJoinRequest
import com.ssafy.domain.model.ChatRoom
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ChatApi {
    @GET("/api/chat/room/list")
    suspend fun getChatList() : List<ChatRoom>

    @Headers("Content-Type: application/json")
    @POST("/api/chat/room/")
    suspend fun postChatCreate(@Body request: ChatCreateRequest ): Response<Unit>

    @Headers("Content-Type: application/json")
    @POST("/api/chat/room/join")
    suspend fun postChatJoin(@Body request: ChatJoinRequest ): Response<Unit>

    @Headers("Content-Type: application/json")
    @POST("/api/chat/room/exit")
    suspend fun postChatExit(@Body request: ChatExitRequest): Response<Unit>
}