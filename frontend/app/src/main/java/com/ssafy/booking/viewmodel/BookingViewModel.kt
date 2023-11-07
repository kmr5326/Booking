package com.ssafy.booking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.booking.model.UserProfileState
import com.ssafy.booking.ui.profile.ProfileData
import com.ssafy.domain.model.booking.BookingAll
import com.ssafy.domain.model.booking.BookingCreateRequest
import com.ssafy.domain.model.booking.BookingDetail
import com.ssafy.domain.model.booking.BookingParticipants
import com.ssafy.domain.model.booking.BookingWaiting
import com.ssafy.domain.model.mypage.AddressnModifyRequest
import com.ssafy.domain.model.mypage.UserDeleteRequest
import com.ssafy.domain.model.mypage.UserFollowersResponse
import com.ssafy.domain.model.mypage.UserFollowingsResponse
import com.ssafy.domain.model.mypage.UserInfoResponse
import com.ssafy.domain.model.mypage.UserModifyRequest
import com.ssafy.domain.usecase.BookingUseCase
import com.ssafy.domain.usecase.MyPageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingUseCase: BookingUseCase
) : ViewModel() {
    private val _postCreateBookingResponse = MutableLiveData<Response<Unit>>()
    val postCreateBookingResponse: LiveData<Response<Unit>> get() = _postCreateBookingResponse
    fun postCreateBooking(request: BookingCreateRequest) =
        viewModelScope.launch {
            _postCreateBookingResponse.value = bookingUseCase.postBookingCreate(request)
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

