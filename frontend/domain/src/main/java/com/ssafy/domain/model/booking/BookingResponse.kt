package com.ssafy.domain.model.booking

import com.google.gson.annotations.SerializedName
import java.io.Serial
import java.time.LocalDate
import java.time.LocalDateTime


// 해시 태그를 위한 데이터 타입
data class HashtagResponse(
    @SerializedName("hashtagId")
    val hashtagId: Long,
    @SerializedName("content")
    val content: String
)

// 모임 목록 전체 조회
data class BookingAll (
    @SerializedName("meetingId")
    val meetingId : Long,
    @SerializedName("bookIsbn")
    val bookIsbn : String,
    @SerializedName("bookTitle")
    val bookTitle : String,
    @SerializedName("coverImage")
    val coverImage : String,
    @SerializedName("meetingTitle")
    val meetingTitle : String,
    @SerializedName("curParticipants")
    val curParticipants : Int,
    @SerializedName("maxParticipants")
    val maxParticipants : Int,
    @SerializedName("lat")
    val lat : Double,
    @SerializedName("lgt")
    val lgt : Double,
    @SerializedName("hashtagList")
    val hashtagList : List<HashtagResponse>,
)

// 모임 상세 조회

data class ParticipantResponse(
    @SerializedName("memberPk")
    val memberPk: Int,
    @SerializedName("loginId")
    val loginId: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profileImage")
    val profileImage: String?,
    @SerializedName("attendanceStatus")
    val attendanceStatus: Boolean,
    @SerializedName("paymentStatus")
    val paymentStatus: Boolean

)

data class MeetingInfoResponse(
    @SerializedName("date")
    val date : LocalDateTime,
    @SerializedName("location")
    val location : String,
    @SerializedName("fee")
    val fee : Int,
    )



data class BookingDetail (
    @SerializedName("meetingId")
    val meetingId : Long,
    @SerializedName("leaderId")
    val leaderId : Int,
    @SerializedName("bookIsbn")
    val bookIsbn : String,
    @SerializedName("bookTitle")
    val bookTitle : String,
    @SerializedName("bookAuthor")
    val bookAuthor : String,
    @SerializedName("bookContent")
    val bookContent : String,
    @SerializedName("coverImage")
    val coverImage : String,
    @SerializedName("meetingTitle")
    val meetingTitle : String,
    @SerializedName("description")
    val description : String,
    @SerializedName("lat")
    val lat : Double,
    @SerializedName("lgt")
    val lgt : Double,
    @SerializedName("maxParticipants")
    val maxParticipants : Int,
    @SerializedName("participantList")
    val participantList : List<ParticipantResponse>,
    @SerializedName("memberPk")
    val memberPk: Int,
    @SerializedName("loginId")
    val loginId: String,
    @SerializedName("nickName")
    val nickName : String,
    @SerializedName("profileImage")
    val profileImage: String?,
    @SerializedName("attendanceStatus")
    val attendanceStatus: Boolean,
    @SerializedName("paymentStatus")
    val paymentStatus: Boolean,

    @SerializedName("hashtagList")
    val hashtagList : List<HashtagResponse>,
    @SerializedName("meetingInfoList")
    val meetingInfoList : List<MeetingInfoResponse>,
    @SerializedName("date")
    val date: LocalDateTime,
    @SerializedName("location")
    val location : String,
    @SerializedName("fee")
    val fee : Int
)

// 모임 참여자 목록
data class BookingParticipants (
    @SerializedName("memberPk")
    val memberPk : Int,
    @SerializedName("loginId")
    val loginId : String,
    @SerializedName("nickname")
    val nickname : String,
    @SerializedName("profileImage")
    val profileImage : String?,

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

// 네이버 검색 api
data class SearchResponse(
    val items : List<SearchItem>
)

data class SearchItem(
    val title : String,
    val link : String,
    val category : String,
    val description : String,
    val telephone : String,
    val address : String,
    val roadAddress : String,
    val mapx : String,
    val mapy : String
)