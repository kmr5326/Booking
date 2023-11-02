package com.ssafy.data.repository

import com.ssafy.data.remote.api.GetChatListApi
import com.ssafy.data.remote.api.PostChatCreateApi
import com.ssafy.domain.model.ChatCreateRequest
import com.ssafy.domain.model.ChatRoom
import com.ssafy.domain.repository.GetChatListRepository
import com.ssafy.domain.repository.PostChatCreateRepository
import retrofit2.Response
import javax.inject.Inject

// 인터페이스 구현체
class GetChatRepositoryImpl @Inject constructor(
    private val getChatListApi: GetChatListApi
) : GetChatListRepository {
    override suspend fun getChatList(): List<ChatRoom> {
        return getChatListApi.getChatList()
    }
}

class PostChatCreateRepositoryImpl @Inject constructor(
    private val postChatCreateApi: PostChatCreateApi
) : PostChatCreateRepository {
    override suspend fun postChatCreate(request: ChatCreateRequest): Response<Int> {
        return postChatCreateApi.postChatCreate(request)
    }
}

