package com.ssafy.data.repository

import com.ssafy.data.remote.api.BookingApi
import com.ssafy.data.remote.api.MyPageApi
import javax.inject.Inject
import com.ssafy.domain.model.booking.BookingCreateRequest
import com.ssafy.domain.repository.BookingRepository
import retrofit2.Response

class BookingRepositoryImpl @Inject constructor(
    private val bookingApi:BookingApi
) : BookingRepository {
    override suspend fun postBookingCreate(request: BookingCreateRequest): Response<Unit> {
        return bookingApi.postBookingCreate(request)
    }
}

