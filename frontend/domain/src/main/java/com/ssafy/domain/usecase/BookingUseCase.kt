package com.ssafy.domain.usecase

import com.ssafy.domain.model.ChatCreateRequest
import com.ssafy.domain.model.ChatExitRequest
import com.ssafy.domain.model.ChatJoinRequest
import com.ssafy.domain.model.booking.BookingCreateRequest
import com.ssafy.domain.repository.BookingRepository
import com.ssafy.domain.repository.ChatRepository
import retrofit2.Response
import javax.inject.Inject

class BookingUseCase @Inject constructor(private val repository: BookingRepository) {
    suspend fun postBookingCreate(request: BookingCreateRequest): Response<Unit> {
        return repository.postBookingCreate(request)
    }
}
