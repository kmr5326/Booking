package com.ssafy.data.repository
import com.ssafy.data.remote.api.BookingApi
import com.ssafy.data.remote.api.MyPageApi
import com.ssafy.domain.model.booking.BookingAll
import javax.inject.Inject
import com.ssafy.domain.model.booking.BookingCreateRequest
import com.ssafy.domain.model.booking.BookingDetail
import com.ssafy.domain.model.booking.BookingParticipants
import com.ssafy.domain.model.booking.BookingWaiting
import com.ssafy.domain.model.booking.SearchResponse
import com.ssafy.domain.repository.BookingRepository
import retrofit2.Response

class BookingRepositoryImpl @Inject constructor(
    private val bookingApi:BookingApi
) : BookingRepository {
    override suspend fun postBookingCreate(request: BookingCreateRequest): Response<Unit> {
        return bookingApi.postBookingCreate(request)
    }
    override suspend fun getAllBooking(): Response<List<BookingAll>> {
        return bookingApi.getAllBooking()
    }
    override suspend fun getEachBooking(meetingId: Long): Response<BookingDetail> {
        return bookingApi.getEachBooking(meetingId)
    }
    override suspend fun getParticipants(meetingId: Long): Response<List<BookingParticipants>> {
        return bookingApi.getParticipants(meetingId)
    }
    override suspend fun getWaitingList(meetingId: Long): Response<List<BookingWaiting>> {
        return bookingApi.getWaitingList(meetingId)
    }

    override suspend fun getSearchList(query:String,display:Int,start:Int,sort:String): Response<SearchResponse> {
        return bookingApi.getSearchList(query,display,start,sort)
    }
}

