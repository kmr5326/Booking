package com.ssafy.domain.usecase

import com.ssafy.domain.model.RecordFileNameRequest
import com.ssafy.domain.model.history.SttResponseDto
import com.ssafy.domain.repository.HistoryRepository
import retrofit2.Response
import javax.inject.Inject

class HistoryUseCase@Inject constructor(private val repository: HistoryRepository) {
    suspend fun postRecordFileName(request: RecordFileNameRequest) : Response<Unit> {
        return repository.postRecordFileName(request)
    }

    suspend fun getSpeakToText(meetinginfoId : Long) : SttResponseDto {
        return repository.getSpeakToText(meetinginfoId)
    }
}