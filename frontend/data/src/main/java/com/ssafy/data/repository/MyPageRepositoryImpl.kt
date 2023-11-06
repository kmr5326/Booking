package com.ssafy.data.repository

import com.ssafy.data.remote.api.MyPageApi
import com.ssafy.domain.model.mypage.AddressnModifyRequest
import com.ssafy.domain.model.mypage.UserDeleteRequest
import com.ssafy.domain.model.mypage.UserFollowersResponse
import com.ssafy.domain.model.mypage.UserFollowingsResponse
import com.ssafy.domain.model.mypage.UserInfoResponse
import com.ssafy.domain.model.mypage.UserModifyRequest
import com.ssafy.domain.repository.MyPageRepository
import retrofit2.Response
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val myPageApi: MyPageApi
) :MyPageRepository {
    override suspend fun getUserInfo(loginId : String) : Response<UserInfoResponse> {
        return myPageApi.getUserInfo(loginId)
    }
    override suspend fun patchUserInfo(request: UserModifyRequest) : Response<Unit> {
        return myPageApi.patchUserInfo(request)
    }
    override suspend fun patchUserAddress(request: AddressnModifyRequest) : Response<Unit> {
        return myPageApi.patchUserAddress(request)
    }
    override suspend fun deleteUser(request: UserDeleteRequest) : Response<Unit> {
        return myPageApi.deleteUser(request)
    }
    override suspend fun getUserFollowers(nickname : String) : Response<UserFollowersResponse> {
        return myPageApi.getUserFollowers(nickname)
    }
    override suspend fun getUserFollowings(nickname : String) : Response<UserFollowingsResponse> {
        return myPageApi.getUserFollowings(nickname)
    }

}