package com.ssafy.booking.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.booking.model.UserInfoChangeResult
import com.ssafy.domain.usecase.NaverCloudUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UploaderViewModel @Inject constructor(
    private val naverCloudUseCase: NaverCloudUseCase,
) : ViewModel() {
    // NAVER CLOUD GET
    private val _naverCloudGetResponse = MutableLiveData<Response<ResponseBody>>()
    val naverCloudGetResponse : LiveData<Response<ResponseBody>> get() = _naverCloudGetResponse

    private val _userInfoChangeResult = MutableStateFlow<UserInfoChangeResult?>(null)
    val userInfoChangeResult: StateFlow<UserInfoChangeResult?> = _userInfoChangeResult.asStateFlow()

    // 파일 요청
    fun GetToNaverCloud(meetingInfoId: String?) =
        viewModelScope.launch {
            _naverCloudGetResponse.value = naverCloudUseCase.getObject("booking-bucket", "recording/${meetingInfoId}_recording.m4a")
        }

    fun enrollRecordFile(loginId: String, meetingInfoId: String, requestBody: RequestBody?) {
        // pImg 는 파일 이름으로 memberPk_profile.png 형식으로 들어갈 예정.
//        val requestInfo = UserModifyRequest(loginId = loginId, profileImage = "${meetingInfoId}_recording.m4a")
//        Log.d("requestInfo", "$requestInfo")

        viewModelScope.launch {
            // requestBody = inputStream data 를 뜻함.
            if(requestBody != null) {
                // 클라우드에 보내기
                naverCloudUseCase.putObject("booking-bucket", "recording/${meetingInfoId}_recording.m4a", requestBody)
            }
            // 서버에 보내기
//            myPageUseCase.patchUserInfo(requestInfo).collect { response ->
//                Log.d("requestInfo", "$response")
//                if (response.isSuccessful) {
//                    // 성공 상태를 StateFlow에 업데이트
//                    _userInfoChangeResult.value = UserInfoChangeResult.Success(nick, "${meetingInfoId}_recording.m4a", "recording/$meetingInfoId")
//                } else {
//                    // 실패 상태를 StateFlow에 업데이트
//                    _userInfoChangeResult.value = UserInfoChangeResult.Error(true)
//                }
//            }
        }
    }
}