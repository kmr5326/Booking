package com.ssafy.domain.usecase.chat

import com.ssafy.domain.model.ChatExitRequest
import com.ssafy.domain.repository.PostChatExitRepository
import retrofit2.Response
import javax.inject.Inject

class ChatExitUseCase @Inject constructor(private val repository: PostChatExitRepository) {
    suspend fun execute(request: ChatExitRequest): Response<Unit> {
        return repository.postChatExit(request)
    }
}