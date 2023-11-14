package com.ssafy.domain.repository

import com.ssafy.domain.model.ChatCreateRequest
import com.ssafy.domain.model.ChatExitRequest
import com.ssafy.domain.model.ChatJoinRequest
import com.ssafy.domain.model.ChatRoom
import com.ssafy.domain.model.LastReadMessageRequest
import com.ssafy.domain.model.MessageResponse
import com.ssafy.domain.model.RecordFileNameRequest
import com.ssafy.domain.model.history.SttResponseDto
import retrofit2.Response
interface HistoryRepository {
    suspend fun postRecordFileName(request: RecordFileNameRequest) : Response<Unit>

    suspend fun getSpeakToText(fileName: String) : SttResponseDto
}