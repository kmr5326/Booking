package com.ssafy.domain.usecase

import com.ssafy.domain.model.mypage.AddressnModifyRequest
import com.ssafy.domain.model.mypage.UserFollowersResponse
import com.ssafy.domain.model.mypage.UserFollowingsResponse
import com.ssafy.domain.model.mypage.UserInfoResponse
import com.ssafy.domain.model.mypage.UserInfoResponseByNickname
import com.ssafy.domain.model.mypage.UserInfoResponseByPk
import com.ssafy.domain.model.mypage.UserModifyRequest
import com.ssafy.domain.repository.MyPageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class MyPageUseCase @Inject constructor(
    private val repository: MyPageRepository
) {
    suspend fun getUserInfo(loginId: String) : Response<UserInfoResponse> {
        return repository.getUserInfo(loginId)
    }
    suspend fun patchUserInfo(request: UserModifyRequest) : Flow<Response<Unit>> =
        flow {
            emit(repository.patchUserInfo(request))
        }

    suspend fun patchUserAddress(request: AddressnModifyRequest) : Response<Unit> {
        return repository.patchUserAddress(request)
    }
    suspend fun deleteUser(loginId: String) : Response<Unit> {
        return repository.deleteUser(loginId)
    }
    suspend fun getUserFollowers(nickname: String) : Response<UserFollowersResponse> {
        return repository.getUserFollowers(nickname)
    }
    suspend fun getUserFollowings(nickname: String) : Response<UserFollowingsResponse> {
        return repository.getUserFollowings(nickname)
    }
    suspend fun getUserInfoByPk(memberPk: Int) : Response<UserInfoResponseByPk> {
        return repository.getUserInfoByPk(memberPk)
    }
    suspend fun getUserInfoByNickname(nickname: String) : Response<UserInfoResponseByNickname> {
        return repository.getUserInfoByNickname(nickname)
    }
    suspend fun postFollow(nickname: String) : Response<Unit> {
        return repository.postFollow(nickname)
    }
    suspend fun deleteFollow(nickname: String) : Response<Unit> {
        return repository.deleteFollow(nickname)
    }
}