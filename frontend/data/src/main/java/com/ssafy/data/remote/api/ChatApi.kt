package com.ssafy.data.remote.api

import com.ssafy.domain.model.ChatCreateRequest
import com.ssafy.domain.model.ChatRoom
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface GetChatListApi {
    @GET("/api/chat/room/list")
    suspend fun getChatList() : List<ChatRoom>
}

interface PostChatCreateApi  {
    @Headers("Content-Type: application/json")
    @POST("/api/chat/room")
    suspend fun postChatCreate(@Body request: ChatCreateRequest ): Response<Int>
}