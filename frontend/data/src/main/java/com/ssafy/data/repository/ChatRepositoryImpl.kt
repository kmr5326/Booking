package com.ssafy.data.repository

import com.ssafy.data.remote.api.GetChatListApi
import com.ssafy.data.remote.api.PostChatCreateApi
import com.ssafy.data.remote.api.PostChatExitApi
import com.ssafy.data.remote.api.PostChatJoinApi
import com.ssafy.domain.model.ChatCreateRequest
import com.ssafy.domain.model.ChatExitRequest
import com.ssafy.domain.model.ChatJoinRequest
import com.ssafy.domain.model.ChatRoom
import com.ssafy.domain.repository.GetChatListRepository
import com.ssafy.domain.repository.PostChatCreateRepository
import com.ssafy.domain.repository.PostChatExitRepository
import com.ssafy.domain.repository.PostChatJoinRepository
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
    override suspend fun postChatCreate(request: ChatCreateRequest): Response<Unit> {
        return postChatCreateApi.postChatCreate(request)
    }
}

class PostChatJoinRepositoryImpl @Inject constructor(
    private val postChatJoinApi: PostChatJoinApi
) : PostChatJoinRepository {
    override suspend fun postChatJoin(request: ChatJoinRequest): Response<Unit> {
        return postChatJoinApi.postChatJoin(request)
    }
}

class PostChatExitRepositoryImpl @Inject constructor(
    private val postChatExitApi: PostChatExitApi
) : PostChatExitRepository {
    override suspend fun postChatExit(request: ChatExitRequest): Response<Unit> {
        return postChatExitApi.postChatExit(request)
    }
}

