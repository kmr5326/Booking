package com.ssafy.data.repository

import com.ssafy.data.remote.api.ChatListApi
import com.ssafy.domain.model.ChatRoom
import com.ssafy.domain.repository.ChatRepository
import javax.inject.Inject

// 인터페이스 구현체
class ChatRepositoryImpl @Inject constructor(
    private val chatListApi: ChatListApi
) : ChatRepository {
    override suspend fun getChatList(): List<ChatRoom> {
        return chatListApi.getChatList()
    }
}
