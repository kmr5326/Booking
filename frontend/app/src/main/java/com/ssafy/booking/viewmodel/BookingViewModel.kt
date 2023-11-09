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
import com.ssafy.domain.model.booking.SearchResponse
import com.ssafy.domain.model.mypage.UserInfoResponse
import com.ssafy.domain.usecase.BookingUseCase
import com.ssafy.domain.usecase.MyPageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingUseCase: BookingUseCase,
    private val firebaseRepositoryImpl: FirebaseRepositoryImpl,
    private val myPageUseCase: MyPageUseCase
) : ViewModel() {

    // POST - 모임 생성
    private val _postCreateBookingResponse = MutableLiveData<Response<Unit>>()
    private val _createBookingSuccess = MutableLiveData<Boolean?>()
    val createBookingSuccess: LiveData<Boolean?> get() = _createBookingSuccess
    val postCreateBookingResponse: LiveData<Response<Unit>> get() = _postCreateBookingResponse
    fun postCreateBooking(request: BookingCreateRequest) =
        viewModelScope.launch {
            val response = bookingUseCase.postBookingCreate(request)
            _postCreateBookingResponse.value = response
            _createBookingSuccess.value = response.isSuccessful // 여기서 모임생성의 성공 여부를 업데이트
        }

    // 모임 생성 여부 초기화
    fun resetCreateBookingSuccess() {
        _createBookingSuccess.value = null
    }

    // POST - 디바이스 토큰 전송
    fun postDeivceToken(deviceToken: DeviceToken) =
        viewModelScope.launch {
            try {
                val response = firebaseRepositoryImpl.postDeviceToken(deviceToken)
                if (response.isSuccessful) {
                    Log.d("DEVICE_TOKEN", "SUCCESS $response")
                } else {
                    Log.d("DEVICE_TOKEN", "ELSE $response")
                }
            } catch (e: Exception) {
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
    val getBookingDetailResponse: LiveData<Response<BookingDetail>> get() = _getBookingDetailResponse
    fun getBookingDetail(meetingId: Long) =
        viewModelScope.launch {
            _getBookingDetailResponse.value = bookingUseCase.getEachBooking(meetingId)
        }

    // GET - 참여자 목록 조회
    private val _getParticipantsResponse = MutableLiveData<Response<List<BookingParticipants>>>()
    val getParticipantsResponse: LiveData<Response<List<BookingParticipants>>> get() = _getParticipantsResponse
    fun getParticipants(meetingId: Long) =
        viewModelScope.launch {
            _getParticipantsResponse.value = bookingUseCase.getParticipants(meetingId)
        }

    // GET - 대기자 목록 조회
    private val _getWaitingListResponse = MutableLiveData<Response<List<BookingWaiting>>>()
    val getWaitingListResponse: LiveData<Response<List<BookingWaiting>>> get() = _getWaitingListResponse
    fun getWaitingList(meetingId: Long) =
        viewModelScope.launch {
            _getWaitingListResponse.value = bookingUseCase.getWaitingList(meetingId)
        }

    // GET - 유저 정보 요청 로직
    private val _getUserInfoResponse = MutableLiveData<Response<UserInfoResponse>>()
    val getUserInfoResponse: LiveData<Response<UserInfoResponse>> get() = _getUserInfoResponse

    fun getUserInfo(loginId: String) =
        viewModelScope.launch {
            _getUserInfoResponse.value = myPageUseCase.getUserInfo(loginId)
        }

    // GET - 네이버 검색 API
    private val _getSearchListResponse = MutableLiveData<Response<SearchResponse>>()
    val getSearchListResponse: LiveData<Response<SearchResponse>> get() = _getSearchListResponse

    fun getSearchList(query: String, display: Int, start: Int, sort: String) =
        viewModelScope.launch {
            _getSearchListResponse.value = bookingUseCase.getSearchList(query, display, start, sort)
        }
}
