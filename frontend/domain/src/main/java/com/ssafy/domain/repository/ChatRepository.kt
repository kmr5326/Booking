package com.ssafy.domain.repository

import com.ssafy.domain.model.ChatCreateRequest
import com.ssafy.domain.model.ChatRoom
import retrofit2.Response

// 인터페이스
interface GetChatListRepository {
    suspend fun getChatList() : List<ChatRoom>
}

interface PostChatCreateRepository {
    suspend fun postChatCreate(request: ChatCreateRequest) : Response<Int>
}
