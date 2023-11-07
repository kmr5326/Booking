package com.ssafy.domain.usecase

import com.ssafy.domain.model.ChatCreateRequest
import com.ssafy.domain.model.ChatExitRequest
import com.ssafy.domain.model.ChatJoinRequest
import com.ssafy.domain.model.booking.BookingAll
import com.ssafy.domain.model.booking.BookingCreateRequest
import com.ssafy.domain.model.booking.BookingDetail
import com.ssafy.domain.model.booking.BookingParticipants
import com.ssafy.domain.model.booking.BookingWaiting
import com.ssafy.domain.model.mypage.UserInfoResponse
import com.ssafy.domain.repository.BookingRepository
import com.ssafy.domain.repository.ChatRepository
import retrofit2.Response
import javax.inject.Inject

class BookingUseCase @Inject constructor(private val repository: BookingRepository) {
    suspend fun postBookingCreate(request: BookingCreateRequest): Response<Unit> {
        return repository.postBookingCreate(request)
    }
    suspend fun getAllBooking() : Response<List<BookingAll>> {
        return repository.getAllBooking()
    }
    suspend fun getEachBooking(meetingId:Long) : Response<BookingDetail> {
        return repository.getEachBooking(meetingId)
    }
    suspend fun getParticipants(meetingId:Long) : Response<List<BookingParticipants>> {
        return repository.getParticipants(meetingId)
    }
    suspend fun getWaitingList(meetingId:Long) : Response<List<BookingWaiting>> {
        return repository.getWaitingList(meetingId)}
}

