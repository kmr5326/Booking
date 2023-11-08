package com.ssafy.booking.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.data.repository.FirebaseRepositoryImpl
import com.ssafy.domain.model.DeviceToken
import com.ssafy.domain.model.booking.BookingAll
import com.ssafy.domain.model.booking.BookingCreateRequest
import com.ssafy.domain.model.booking.BookingDetail
import com.ssafy.domain.model.booking.BookingParticipants
import com.ssafy.domain.model.booking.BookingWaiting
import com.ssafy.domain.usecase.BookingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingUseCase: BookingUseCase,
    private val firebaseRepositoryImpl: FirebaseRepositoryImpl
) : ViewModel() {
    private val _postCreateBookingResponse = MutableLiveData<Response<Unit>>()
    val postCreateBookingResponse: LiveData<Response<Unit>> get() = _postCreateBookingResponse
    fun postCreateBooking(request: BookingCreateRequest) =
        viewModelScope.launch {
            _postCreateBookingResponse.value = bookingUseCase.postBookingCreate(request)
        }

    // POST - 디바이스 토큰 전송
    fun postDeivceToken(deviceToken: DeviceToken) =
        viewModelScope.launch {
            try {
                val response = firebaseRepositoryImpl.postDeviceToken(deviceToken)
                if(response.isSuccessful) {
                    Log.d("DEVICE_TOKEN", "SUCCESS ${response}")
                } else {
                    Log.d("DEVICE_TOKEN", "ELSE ${response}")
                }
            } catch(e : Exception) {
                Log.e("DEVICE_TOKEN", "ERROR")
            }
        }

    // GET - 모임 전체 목록 조회
    private val _getBookingAllListResponse = MutableLiveData<Response<List<BookingAll>>>()
    val getBookingAllList: LiveData<Response<List<BookingAll>>> get() = _getBookingAllListResponse

    fun getBookingAllList() =
        viewModelScope.launch {
            _getBookingAllListResponse.value = bookingUseCase.getAllBooking()
        }

    // GET - 모임 상세 조회
    private val _getBookingDetailResponse = MutableLiveData<Response<BookingDetail>>()
    val getBookingDetail: LiveData<Response<BookingDetail>> get() = _getBookingDetailResponse
    fun getBookingDetail(meetingId: Long) =
        viewModelScope.launch {
            _getBookingDetailResponse.value = bookingUseCase.getEachBooking(meetingId)
        }

    // GET - 참여자 목록 조회
    private val _getParticipantsResponse = MutableLiveData<Response<List<BookingParticipants>>>()
    val getParticipants: LiveData<Response<List<BookingParticipants>>> get() = _getParticipantsResponse
    fun getParticipants(meetingId: Long) =
        viewModelScope.launch {
            _getParticipantsResponse.value = bookingUseCase.getParticipants(meetingId)
        }

    // GET - 대기자 목록 조회
    private val _getWaitingListResponse = MutableLiveData<Response<List<BookingWaiting>>>()
    val getWaitingList: LiveData<Response<List<BookingWaiting>>> get() = _getWaitingListResponse
    fun getWaitingList(meetingId: Long) =
        viewModelScope.launch {
            _getWaitingListResponse.value = bookingUseCase.getWaitingList(meetingId)
        }
}

