package com.ssafy.domain.repository

import com.ssafy.domain.model.ChatCreateRequest
import retrofit2.Response
import com.ssafy.domain.model.booking.BookingCreateRequest

interface BookingRepository {
    suspend fun postBookingCreate(request : BookingCreateRequest) : Response<Unit>
}