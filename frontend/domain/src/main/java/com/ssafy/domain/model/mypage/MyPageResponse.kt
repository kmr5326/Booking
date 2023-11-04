package com.ssafy.domain.model.mypage

import com.google.gson.annotations.SerializedName

data class UserInfoResponse(

    @SerializedName("loginId")
    val loginId : String,
    @SerializedName("email")
    val email : String,
    @SerializedName("age")
    val age : Int,
    @SerializedName("gender")
    val gender : String,
    @SerializedName("nickname")
    val nickname : String,
    @SerializedName("fullname")
    val fullname : String,
    @SerializedName("address")
    val address : String,
    @SerializedName("profileImage")
    val profileImage : String,
    @SerializedName("provider")
    val provider : String,
)

data class FollowersList (

    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profileImage")
    val profileImage: String
)

data class UserFollowersResponse (

    @SerializedName("followers")
    val followers: List<FollowersList>,
    @SerializedName("followersCnt")
    val followersCnt: Int
)

data class UserFollowingsResponse (

    @SerializedName("followings")
    val followings: List<FollowersList>,
    @SerializedName("followingsCnt")
    val followingsCnt: Int
)

