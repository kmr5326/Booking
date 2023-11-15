package com.ssafy.data.remote.api

import com.ssafy.domain.model.ChatCreateRequest
import com.ssafy.domain.model.RecordFileNameRequest
import com.ssafy.domain.model.history.SttResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HistoryAPi {
    @Headers("Content-Type: application/json")
    @POST("/api/booking/stt")
    suspend fun postRecordFileName(@Body request: RecordFileNameRequest): Response<Unit>

    @GET("/api/booking/stt")
    suspend fun getSpeakToText(
        @Query("meetingInfoId") meetinginfoId: Long,
    ): SttResponseDto
}