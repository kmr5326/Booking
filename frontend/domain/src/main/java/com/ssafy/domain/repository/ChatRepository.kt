package com.ssafy.domain.repository

import com.ssafy.domain.model.ChatCreateRequest
import com.ssafy.domain.model.ChatExitRequest
import com.ssafy.domain.model.ChatJoinRequest
import com.ssafy.domain.model.ChatRoom
import retrofit2.Response

// 인터페이스
interface ChatRepository {
    suspend fun getChatList() : List<ChatRoom>
    suspend fun postChatCreate(request: ChatCreateRequest) : Response<Unit>
    suspend fun postChatJoin(request: ChatJoinRequest) : Response<Unit>
    suspend fun postChatExit(request: ChatExitRequest) : Response<Unit>

}
