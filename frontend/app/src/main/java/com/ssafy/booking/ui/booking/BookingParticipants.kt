package com.ssafy.booking.ui.booking

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.ssafy.booking.viewmodel.BookingViewModel
import com.ssafy.domain.model.booking.BookingDetail
import com.ssafy.domain.model.booking.BookingParticipants
import com.ssafy.domain.model.booking.BookingWaiting

@Composable
fun BookingParticipants(meetingId:Long) {

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

    // 참가자 바뀔 떄마다 업데이트
    LaunchedEffect(getParticipantsResponse) {
        getParticipantsResponse?.body()?.let { response ->
            Log.d("test334", "$response")
            participantsList = response // 상태 업데이트
        }
    }

    // 대기자 바뀔 때마다 업데이트
    LaunchedEffect(getWaitingListResponse) {
        getWaitingListResponse?.body()?.let { response ->
            Log.d("test334", "$response")
            waitingList = response // 상태 업데이트
        }
    }
//    participantsList = newList // 새로운 참가자 리스트
//    waitingList = newWaitingList // 새로운 대기자 리스트 (예비로 작성한 코드)
    Column {
        Text(text = "참가자 목록")
        participantsList.forEach { participant ->
            ParticipantItem(participant = participant)
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        Text(text = "대기자 목록")
        waitingList.forEach { waiting ->
            WaitingListItem(waiting = waiting)
        }
    }
}

// 참가자
@Composable
fun ParticipantItem(participant: BookingParticipants) {
    // 참가자 한 명에 대한 UI를 여기에 구성하세요.
    Row(verticalAlignment = Alignment.CenterVertically) {
        // 예를 들어 프로필 이미지가 있다면:
        participant.profileImage?.let { imageUrl ->
            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = "Profile Image",
                modifier = Modifier.size(40.dp).clip(CircleShape)
            )
        }
        // 닉네임과 다른 정보를 표시
        Column {
            Text(text = participant.nickname, fontWeight = FontWeight.Bold)
            Text(text = if (participant.attendanceStatus) "출석" else "미출석")
            Text(text = if (participant.paymentStatus) "결제 완료" else "미결제")

        }


    }
}

// 대기자

@Composable
fun WaitingListItem(waiting: BookingWaiting) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // 이미지 로딩 로직은 ParticipantItem과 유사
        Image(
            painter = rememberImagePainter(waiting.profileImage),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        // 닉네임 표시
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(text = waiting.nickname, fontWeight = FontWeight.Bold)
        }
    }
}