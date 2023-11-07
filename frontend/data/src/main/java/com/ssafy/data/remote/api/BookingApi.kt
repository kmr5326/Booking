package com.ssafy.data.remote.api
import com.ssafy.domain.model.booking.BookingAll
import com.ssafy.domain.model.booking.BookingCreateRequest
import com.ssafy.domain.model.booking.BookingDetail
import com.ssafy.domain.model.booking.BookingParticipants
import com.ssafy.domain.model.booking.BookingWaiting
import com.ssafy.domain.model.mypage.UserInfoResponse
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface BookingApi {
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/api/booking/meeting/")
    suspend fun postBookingCreate(@Body request:BookingCreateRequest): Response<Unit>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/booking/meeting/")
    suspend fun getAllBooking() : Response<List<BookingAll>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/booking/meeting/{meetingId}")
    suspend fun getEachBooking(@Path("meetingId") meetingId:Long) : Response<BookingDetail>

    @Headers("Content-Type: application/json;charset=UTF-8")
@GET("/api/booking/participant/{meetingId}")
suspend fun getParticipants(@Path("meetingId") meetingId:Long) : Response<List<BookingParticipants>>

@Headers("Content-Type: application/json;charset=UTF-8")
@GET("/api/booking/waitlist/{meetingId}")
suspend fun getWaitingList(@Path("meetingId") meetingId:Long) : Response<List<BookingWaiting>>
}
