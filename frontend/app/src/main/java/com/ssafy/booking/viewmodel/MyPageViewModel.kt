package com.ssafy.booking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.booking.model.UserProfileState
import com.ssafy.booking.ui.profile.ProfileData
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

    // 마이페이지 에러 핸들링
    private val _profileState = MutableLiveData<UserProfileState>()
    val profileState: LiveData<UserProfileState> = _profileState

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


    // 최종 마이페이지 작업 로직
    fun getMyPage(loginId: String) = viewModelScope.launch {
        _profileState.value = UserProfileState.Loading

        try {
            val userInfoResponse = myPageUseCase.getUserInfo(loginId)
            if (userInfoResponse.isSuccessful && userInfoResponse.body() != null) {
                // loginId 를 통한 유저 정보 조회 완료 시 - 팔로워 팔로잉 목록 조회
                userInfoResponse.body()?.nickname?.let { nickname ->
                    val userFollowersResponse = myPageUseCase.getUserFollowers(nickname)
                    val userFollowingsResponse = myPageUseCase.getUserFollowings(nickname)

                    if (userFollowersResponse.isSuccessful && userFollowingsResponse.isSuccessful
                        && userFollowersResponse.body() != null && userFollowingsResponse.body() != null) {
                        val profileData = ProfileData(
                            myProfile = userInfoResponse.body(),
                            followers = userFollowersResponse.body(),
                            followings = userFollowingsResponse.body()
                            // readBookNumber는 예시에 없으므로 기본값 0을 사용
                        )
                        _profileState.value = UserProfileState.Success(profileData)
                    } else {
                        _profileState.value = UserProfileState.Error("Failed to fetch followers && followings.")
                        return@launch
                    }
                } ?: run {
                    _profileState.value = UserProfileState.Error("nickname is null")
                }
            } else {
                _profileState.value = UserProfileState.Error("Failed to fetch user info.")
            }
        } catch (e: Exception) {
            _profileState.value = UserProfileState.Error(e.message ?: "Unknown error occurred")
        }
    }

}