package com.ssafy.domain.usecase

import com.ssafy.domain.model.mypage.AddressnModifyRequest
import com.ssafy.domain.model.mypage.UserDeleteRequest
import com.ssafy.domain.model.mypage.UserFollowersResponse
import com.ssafy.domain.model.mypage.UserFollowingsResponse
import com.ssafy.domain.model.mypage.UserInfoResponse
import com.ssafy.domain.model.mypage.UserModifyRequest
import com.ssafy.domain.repository.MyPageRepository
import retrofit2.Response
import javax.inject.Inject

class MyPageUseCase @Inject constructor(
    private val repository: MyPageRepository
) {
    suspend fun getUserInfo(loginId: String) : Response<UserInfoResponse> {
        return repository.getUserInfo(loginId)
    }
    suspend fun patchUserInfo(request: UserModifyRequest) : Response<Unit> {
        return repository.patchUserInfo(request)
    }
    suspend fun patchUserAddress(request: AddressnModifyRequest) : Response<Unit> {
        return repository.patchUserAddress(request)
    }
    suspend fun deleteUser(request: UserDeleteRequest) : Response<Unit> {
        return repository.deleteUser(request)
    }
    suspend fun getUserFollowers(nickname: String) : Response<UserFollowersResponse> {
        return repository.getUserFollowers(nickname)
    }
    suspend fun getUserFollowings(nickname: String) : Response<UserFollowingsResponse> {
        return repository.getUserFollowings(nickname)
    }
}