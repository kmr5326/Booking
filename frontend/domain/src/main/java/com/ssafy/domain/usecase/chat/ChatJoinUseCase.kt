package com.ssafy.domain.usecase.chat

import com.ssafy.domain.model.ChatJoinRequest
import com.ssafy.domain.repository.PostChatJoinRepository
import retrofit2.Response
import javax.inject.Inject

class ChatJoinUseCase @Inject constructor(private val repository: PostChatJoinRepository) {
    suspend fun execute(request: ChatJoinRequest): Response<Unit> {
        return repository.postChatJoin(request)
    }
}