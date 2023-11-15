package com.ssafy.data.repository

import com.ssafy.data.remote.api.HistoryAPi
import com.ssafy.domain.model.RecordFileNameRequest
import com.ssafy.domain.model.history.SttResponseDto
import com.ssafy.domain.repository.HistoryRepository
import retrofit2.Response
import javax.inject.Inject

class HistoryRepositoryImpl  @Inject constructor(private val historyAPi: HistoryAPi) : HistoryRepository {
    override suspend fun postRecordFileName(request: RecordFileNameRequest): Response<Unit> {
        return historyAPi.postRecordFileName(request)
    }

    override suspend fun getSpeakToText(meetinginfoId : Long) : SttResponseDto {
        return historyAPi.getSpeakToText(meetinginfoId)
    }
}


