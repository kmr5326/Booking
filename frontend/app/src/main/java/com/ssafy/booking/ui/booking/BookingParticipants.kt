package com.ssafy.booking.ui.booking

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.MaterialTheme.colors
import coil.compose.rememberImagePainter
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.viewmodel.BookingViewModel
import com.ssafy.domain.model.booking.BookingAcceptRequest
import com.ssafy.domain.model.booking.BookingDetail
import com.ssafy.domain.model.booking.BookingParticipants
import com.ssafy.domain.model.booking.BookingRejectRequest
import com.ssafy.domain.model.booking.BookingWaiting

@Composable
fun BookingParticipants( meetingId : Long,
                         memberRole : String,
                         meetingState : String,
                         ) {
    // 뷰모델 연결
    val bookingViewModel: BookingViewModel = hiltViewModel()
    val getParticipantsResponse by bookingViewModel.getParticipantsResponse.observeAsState()
    val getWaitingListResponse by bookingViewModel.getWaitingListResponse.observeAsState()
    var participantsList by remember { mutableStateOf<List<BookingParticipants>>(emptyList()) }
    var waitingList by remember { mutableStateOf<List<BookingWaiting>>(emptyList()) }

    LaunchedEffect(Unit) {
        bookingViewModel.getWaitingList(meetingId)
        bookingViewModel.getParticipants(meetingId)
    }
//
    // 참가자 바뀔 떄마다 업데이트
    LaunchedEffect(getParticipantsResponse) {
        getParticipantsResponse?.body()?.let { response ->
            Log.d("참가대기자", "$response")
            var sortByParticipantsList = response.sortedBy { it.memberPk }
            participantsList = sortByParticipantsList // 상태 업데이트
        }
    }

    // 대기자 바뀔 때마다 업데이트
    LaunchedEffect(getWaitingListResponse) {
        getWaitingListResponse?.body()?.let { response ->
            Log.d("참가대기자", "$response")
            var sortByWaitingList = response.sortedBy { it.memberPk }
            waitingList = sortByWaitingList // 상태 업데이트
        }
    }
    Column {
        Text(text = "참가자 목록")
        participantsList.forEach { participant ->
            ParticipantItem(participant = participant)
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))
        // 방장이면서 모임이 준비중일 때만 대기자 목록을 표시
        if (memberRole == "LEADER" && meetingState == "PREPARING") {
            Text(text = "대기자 목록")
            waitingList.forEach { waiting ->
                WaitingListItem(
                    meetingId = meetingId,
                    waiting = waiting,
                    bookingViewModel = bookingViewModel
                )
            }
        }
    }
}
// 참가자
@Composable
fun ParticipantItem(participant: BookingParticipants) {
    val navController = LocalNavigation.current

    // 참가자 한 명에 대한 UI를 여기에 구성하세요.
    Row(verticalAlignment = Alignment.CenterVertically) {
        // 프로필 이미지가 있다면:
        participant.profileImage?.let { imageUrl ->
            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = "Profile Image",
                modifier = Modifier.size(40.dp).clip(CircleShape)
            )
        }
        // 닉네임과 다른 정보를 표시
        Column(
            modifier = Modifier.clickable{
                navController.navigate("profile/${participant.memberPk}"){
                    popUpTo("login") { inclusive = true }
                }
            }
        ) {
            Text(text = participant.nickname, fontWeight = FontWeight.Bold)
            Text(text = if (participant.attendanceStatus) "출석" else "미출석")
            Text(text = if (participant.paymentStatus) "결제 완료" else "미결제")
        }
    }
}

// 대기자

@Composable
fun WaitingListItem(waiting: BookingWaiting, meetingId: Long, bookingViewModel: BookingViewModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween, // 아이템들을 양 끝으로 정렬
        modifier = Modifier.fillMaxWidth() // Row의 너비를 부모에 맞춤
    ) {
        // 프로필 이미지와 닉네임을 포함하는 섹션
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberImagePainter(waiting.profileImage),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(text = waiting.nickname, fontWeight = FontWeight.Bold)
            }
        }
        // 버튼들을 포함하는 섹션
        Row {
            // 체크 버튼
            Button(
                onClick = {
                    val request =
                        BookingAcceptRequest(meetingId = meetingId, memberId = waiting.memberPk)
                    bookingViewModel.postBookingAccept(meetingId, waiting.memberPk, request)
                    // 다시 쏴서 리스트 갱신해
                    bookingViewModel.getParticipants(meetingId)
                    bookingViewModel.getWaitingList(meetingId)
                }
                , // 여기서는 대기자의 ID를 파라미터로 전달
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Approve",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp)) // 버튼 사이의 간격 추가
            // 엑스 버튼
            Button(
                onClick = {
                    val request = BookingRejectRequest(meetingId = meetingId, memberId = waiting.memberPk)
                    bookingViewModel.postBookingReject(meetingId,waiting.memberPk,request)
                    // 다시 쏴서 리스트 갱신해
                    bookingViewModel.getParticipants(meetingId)
                    bookingViewModel.getWaitingList(meetingId)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Reject",
                    tint = Color.White
                )
            }
        }
    }
}