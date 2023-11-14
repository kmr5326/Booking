package com.ssafy.data.remote.api

import com.ssafy.domain.model.ChatCreateRequest
import com.ssafy.domain.model.RecordFileNameRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface HistoryAPi {
    @Headers("Content-Type: application/json")
    @POST("/api/booking/stt")
    suspend fun postRecordFileName(@Body request: RecordFileNameRequest): Response<Unit>
}