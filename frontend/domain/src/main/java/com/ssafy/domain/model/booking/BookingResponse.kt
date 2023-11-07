package com.ssafy.domain.model.booking

import com.google.gson.annotations.SerializedName


// 해시 태그를 위한 데이터 타입
data class Hashtag(
    @SerializedName("hashtagId")
    val hashtagId: Int,
    @SerializedName("content")
    val content: String
)

// 모임 목록 전체 조회


data class BookingAll (
    @SerializedName("meetingId")
    val meetingId : Long,
    @SerializedName("leaderId")
    val leaderId : Int,
    @SerializedName("bookIsbn")
    val bookIsbn : String,
    @SerializedName("meetingTitle")
    val meetingTitle : String,
    @SerializedName("description")
    val description : String,
    @SerializedName("maxParticipants")
    val maxParticipants : Int,
    @SerializedName("hashtagList")
    val hashtagList : List<Hashtag>,
)

// 모임 상세 조회
data class BookingDetail (
    @SerializedName("meetingId")
    val meetingId : Long,
    @SerializedName("leaderId")
    val leaderId : Int,
    @SerializedName("bookIsbn")
    val bookIsbn : String,
    @SerializedName("meetingTitle")
    val meetingTitle : String,
    @SerializedName("description")
    val description : String,
    @SerializedName("maxParticipants")
    val maxParticipants : Int,
    @SerializedName("hashtagList")
    val hashtagList : List<Hashtag>,
)

// 모임 참여자 목록
data class BookingParticipants (
    @SerializedName("loginId")
    val loginId : String,
    @SerializedName("nickname")
    val nickname : String,
    @SerializedName("profileImage")
    val profileImage : String?,
    @SerializedName("memberPk")
    val memberPk : Int,
    @SerializedName("attendanceStatus")
    val attendanceStatus : Boolean,
    @SerializedName("paymentStatus")
    val paymentStatus : Boolean,

)


// 모임 대기자 목록

data class BookingWaiting (
    @SerializedName("loginId")
    val loginId : String,
    @SerializedName("nickname")
    val nickname : String,
    @SerializedName("profileImage")
    val profileImage : String,
    @SerializedName("memberPk")
    val memberPk : Int
)