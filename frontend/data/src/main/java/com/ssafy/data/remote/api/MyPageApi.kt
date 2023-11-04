package com.ssafy.data.remote.api

import com.ssafy.domain.model.mypage.AddressnModifyRequest
import com.ssafy.domain.model.mypage.UserDeleteRequest
import com.ssafy.domain.model.mypage.UserFollowersResponse
import com.ssafy.domain.model.mypage.UserFollowingsResponse
import com.ssafy.domain.model.mypage.UserInfoResponse
import com.ssafy.domain.model.mypage.UserModifyRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.Path

interface MyPageApi {

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/members/memberInfo/{loginId}")
    suspend fun getUserInfo(@Path("loginId") loginId: String) : Response<UserInfoResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @PATCH("/api/members/modification")
    suspend fun patchUserInfo(@Body request: UserModifyRequest) : Response<Unit>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @PATCH("/api/members/modification") // 나중에 바뀔 수도 ?? 이건 한결이한테 물어보기
    suspend fun patchUserAddress(@Body request: AddressnModifyRequest) : Response<Unit>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @DELETE("/api/members/deletion")
    suspend fun deleteUser(@Body request: UserDeleteRequest) : Response<Unit>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/follows/followers/{nickname}")
    suspend fun getUserFollowers(@Path("nickname") nickname: String) : Response<UserFollowersResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/follows/followings/{nickname}")
    suspend fun getUserFollowings(@Path("nickname") nickname: String) : Response<UserFollowingsResponse>

}