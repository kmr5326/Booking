package com.ssafy.domain.repository

import com.ssafy.domain.model.mypage.AddressnModifyRequest
import com.ssafy.domain.model.mypage.UserDeleteRequest
import com.ssafy.domain.model.mypage.UserFollowersResponse
import com.ssafy.domain.model.mypage.UserFollowingsResponse
import com.ssafy.domain.model.mypage.UserInfoResponse
import com.ssafy.domain.model.mypage.UserModifyRequest
import retrofit2.Response

interface MyPageRepository {
    suspend fun getUserInfo(loginId: String) : Response<UserInfoResponse>
    suspend fun patchUserInfo(request: UserModifyRequest) : Response<Unit>
    suspend fun patchUserAddress(request: AddressnModifyRequest) : Response<Unit>
    suspend fun deleteUser(request: UserDeleteRequest) : Response<Unit>
    suspend fun getUserFollowers(nickname: String) : Response<UserFollowersResponse>
    suspend fun getUserFollowings(nickname: String) : Response<UserFollowingsResponse>
}