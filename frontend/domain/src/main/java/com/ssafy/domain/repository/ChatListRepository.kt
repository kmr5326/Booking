package com.ssafy.domain.repository

import com.ssafy.domain.model.ChatRoom

// 인터페이스
interface ChatRepository {
    suspend fun getChatList() : List<ChatRoom>
}


