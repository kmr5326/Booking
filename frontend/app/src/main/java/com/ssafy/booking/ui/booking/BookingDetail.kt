
package com.ssafy.booking.ui.booking

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ssafy.booking.ui.AppNavItem
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.ui.common.BackTopBar
import com.ssafy.booking.ui.common.TabBar
import com.ssafy.booking.viewmodel.BookingViewModel
import com.ssafy.domain.model.booking.BookingJoinRequest

val tabTitles = listOf("모임 정보", "참가자", "게시판")


@Composable
fun BookingDetail(meetingId: Long) {
    Column {
        BackTopBar(title = "모임 상세")
        TabBar(
            tabTitles,
            contentForTab = { index ->
                when (index) {
                    0 -> BookingInfo(
                        meetingId = meetingId
                    )

                    1 -> BookingParticipants(
                        meetingId = meetingId
                    )

                    2 -> BookingBoard(
                        meetingId = meetingId
                    )
                }
            }
        )
        BookingJoinRequestButton(meetingId = meetingId)

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingJoinRequestButton(meetingId: Long) {
    val context = LocalContext.current
    val bookingViewModel: BookingViewModel = hiltViewModel()
    val postBookingJoinResponse by bookingViewModel.postBookingJoinResponse.observeAsState()
    val navController = LocalNavigation.current

    Scaffold(
        bottomBar = {
            // 하단 바에 버튼 추가
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    navController.navigate(AppNavItem.BookingSetTitle.route)
                    // 모임 수정 관련 로직
                }) {
                    Text(text = "모임 수정")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    navController.navigate(AppNavItem.BookingSetLocation.route)
                    // 모임 확정 관련 로직
                }) {
                    Text(text = "모임 확정")
                }
            }
        }
    ) { paddingValues ->
        // 주요 콘텐츠
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
//            Button(onClick = {
//                navController.navigate(AppNavItem.BookingSetLocation.route)
//            }) {
//                Text(text = "로케이트")
//            }
//            Button(onClick = {
//                navController.navigate(AppNavItem.BookingSetTitle.route)
//            }) {
//                Text(text = "타이틀")
//            }
//            Button(onClick = {
//                navController.navigate(AppNavItem.BookingSetDateAndFee.route)
//            }) {
//                Text(text = "데이트타임")
//            }
        }
    }
}
//    Button(onClick = {
//        val request = BookingJoinRequest(meetingId = meetingId)
//        Log.d("test334", "$request")
//        bookingViewModel.postBookingJoin(meetingId,request)
//        Toast.makeText(context, "참가신청이 완료됐습니다.", Toast.LENGTH_LONG).show()
//    },
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(50.dp)
//    )
//    {
//        Text("모임 참가 신청하기")
//    }
