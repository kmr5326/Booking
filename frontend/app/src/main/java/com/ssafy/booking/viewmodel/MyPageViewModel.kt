package com.ssafy.booking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.domain.model.mypage.AddressnModifyRequest
import com.ssafy.domain.model.mypage.UserDeleteRequest
import com.ssafy.domain.model.mypage.UserFollowersResponse
import com.ssafy.domain.model.mypage.UserFollowingsResponse
import com.ssafy.domain.model.mypage.UserInfoResponse
import com.ssafy.domain.model.mypage.UserModifyRequest
import com.ssafy.domain.usecase.MyPageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val myPageUseCase: MyPageUseCase
) : ViewModel() {

    // GET - 유저 정보 요청 로직
    private val _getUserInfoResponse = MutableLiveData<Response<UserInfoResponse>>()
    val getUserInfoResponse: LiveData<Response<UserInfoResponse>> get() = _getUserInfoResponse

    fun getUserInfo(loginId: String) =
        viewModelScope.launch {
            _getUserInfoResponse.value = myPageUseCase.getUserInfo(loginId)
        }



    // PATCH - 회원 정보 수정 요청 로직
    private val _patchUserInfoResponse = MutableLiveData<Response<Unit>>()
    val patchUserInfoResponse: LiveData<Response<Unit>> get() = _patchUserInfoResponse

    fun patchUserinfo(request: UserModifyRequest) =
        viewModelScope.launch {
            _patchUserInfoResponse.value = myPageUseCase.patchUserInfo(request)
        }

    // PATCH - 회원 위치 정보 수정 요청 로직
    private val _patchUserAddressResponse = MutableLiveData<Response<Unit>>()
    val patchUserAddressResponse: LiveData<Response<Unit>> get() = _patchUserAddressResponse

    fun patchUserAddress(request: AddressnModifyRequest) =
        viewModelScope.launch {
            _patchUserAddressResponse.value = myPageUseCase.patchUserAddress(request)
        }


    // POST - 회원 탈퇴 요청 로직
    private val _postUserDeleteResponse = MutableLiveData<Response<Unit>>()
    val postUserDeleteResponse: LiveData<Response<Unit>> get() = _postUserDeleteResponse

    fun postUserDelete(request: UserDeleteRequest) =
        viewModelScope.launch {
            _postUserDeleteResponse.value = myPageUseCase.deleteUser(request)
        }

    // GET - 팔로워 수 요청 로직
    private val _getUserFollowersResponse = MutableLiveData<Response<UserFollowersResponse>>()
    val getUserFollowersResponse: LiveData<Response<UserFollowersResponse>> get() = _getUserFollowersResponse

    fun getUserFollowers(nickname: String) =
        viewModelScope.launch {
            _getUserFollowersResponse.value = myPageUseCase.getUserFollowers(nickname)
        }

    // GET - 팔로잉 수 요청 로직
    private val _getUserFollowingsResponse = MutableLiveData<Response<UserFollowingsResponse>>()
    val getUserFollowingsResponse: LiveData<Response<UserFollowingsResponse>> get() = _getUserFollowingsResponse

    fun getUserFollowings(nickname: String) =
        viewModelScope.launch {
            _getUserFollowingsResponse.value = myPageUseCase.getUserFollowings(nickname)
        }
}