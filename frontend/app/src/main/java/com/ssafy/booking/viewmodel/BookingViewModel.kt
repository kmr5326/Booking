package com.ssafy.booking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.booking.model.UserProfileState
import com.ssafy.booking.ui.profile.ProfileData
import com.ssafy.domain.model.booking.BookingCreateRequest
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
}

