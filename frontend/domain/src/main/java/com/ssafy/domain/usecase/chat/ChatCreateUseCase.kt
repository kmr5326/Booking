package com.ssafy.domain.usecase.chat

import com.ssafy.domain.model.ChatCreateRequest
import com.ssafy.domain.repository.PostChatCreateRepository
import retrofit2.Response
import javax.inject.Inject

class ChatCreateUseCase @Inject constructor(private val repository: PostChatCreateRepository) {
    suspend fun execute(request: ChatCreateRequest): Response<Unit> {
        return repository.postChatCreate(request)
    }
}