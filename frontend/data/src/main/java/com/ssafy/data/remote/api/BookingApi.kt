package com.ssafy.data.remote.api
import com.ssafy.domain.model.booking.BookingCreateRequest
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface BookingApi {
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/api/booking/meeting/")
    suspend fun postBookingCreate(@Body request:BookingCreateRequest): Response<Unit>
}
