
package com.ssafy.booking.ui.booking

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ssafy.booking.di.App
import com.ssafy.booking.ui.AppNavItem
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.ui.common.BackTopBar
import com.ssafy.booking.ui.common.TabBar
import com.ssafy.booking.viewmodel.BookingViewModel
import com.ssafy.domain.model.booking.BookingJoinRequest

val tabTitles = listOf("모임 정보", "참가자", "게시판")


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetail(meetingId: Long) {
    val bookingViewModel: BookingViewModel = hiltViewModel()
    val getBookingDetailResponse by bookingViewModel.getBookingDetailResponse.observeAsState()
    val context = LocalContext.current
    val navController = LocalNavigation.current
    
    // 리더인지 아닌지
    var isLeadered = false
    // 모임 진행 상황
    val status = getBookingDetailResponse?.body()?.meetingState
    var leaderId : Int? = null
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(getBookingDetailResponse?.body()?.leaderId) {
        bookingViewModel.getBookingDetail(meetingId)
        val memberPk = App.prefs.getMemberPk()
        leaderId = getBookingDetailResponse?.body()?.leaderId
        if (memberPk == leaderId?.toLong()) {
            isLeadered = true
            Log.d("같냐", "같다")
        }
        else {
            isLeadered = false
            Log.d("같냐",memberPk.toString())
            Log.d("같냐",leaderId.toString())
            Log.d("같냐", "다르다")
            Log.d("같냐", getBookingDetailResponse?.body()?.leaderId.toString())

        }
//        isLeadered = memberPk == leaderId?.toLong()
    }
    Scaffold(
        bottomBar = {
            if (status == "PREPARING") {
                if (isLeadered) {
                    PreparingBottomBarForLeader(navController)
                } else {
                    PreparingBottomBarForParticipant(meetingId, bookingViewModel, context)
                }
            }
            if (status == "ONGOING") {
                OngoingBottomBar(meetingId,bookingViewModel,context,isLeadered)

            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            BackTopBar(title = "모임 상세")
            TabBar(
                tabTitles,
                contentForTab = { index ->
                    when (index) {
                        0 -> BookingInfo(
                            meetingId = meetingId,
                            isLeadered = isLeadered
                        )
                        1 -> BookingParticipants(
                            meetingId = meetingId,
                            isLeadered = isLeadered,
                            status = status!!
                        )
                        2 -> BookingBoard(
                            meetingId = meetingId,
                            isLeadered = isLeadered
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun PreparingBottomBarForLeader(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            navController.navigate(AppNavItem.BookingSetTitle.route)
        }) {
            Text(text = "모임 수정")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            navController.navigate(AppNavItem.BookingSetLocation.route)
        }) {
            Text(text = "모임 확정")
        }
    }
}

@Composable
fun PreparingBottomBarForParticipant(meetingId: Long, bookingViewModel: BookingViewModel, context: Context) {
    Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 모임 참가신청
        Button(onClick = {
            val request = BookingJoinRequest(meetingId = meetingId)
            bookingViewModel.postBookingJoin(meetingId, request)
            val toastTop = Toast.makeText(context, "참가신청이 완료됐습니다.", Toast.LENGTH_LONG)
            toastTop.setGravity(Gravity.TOP,0,0)
            toastTop.show()
        },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("모임 참가 신청하기")
        }
        
        // 모임 나가기
        Button(onClick = {
            bookingViewModel.postBookingExit(meetingId)
            val toastTop = Toast.makeText(context, "모임나가기.", Toast.LENGTH_LONG)
            toastTop.setGravity(Gravity.TOP,0,0)
            toastTop.show()
        },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("모임 나가기")
        }
    }
}
@Composable
fun OngoingBottomBar(meetingId:Long,bookingViewModel: BookingViewModel,context: Context,isLeadered:Boolean){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 출석체크 버튼
        Button(onClick = {
            bookingViewModel.patchBookingAttend(meetingId)

            val toastTop = Toast.makeText(context, "출석체크가 완료되었습니다.", Toast.LENGTH_LONG)
            toastTop.setGravity(Gravity.TOP,0,0)
            toastTop.show()
        },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("출석 체크하기")
        }
        // 방장일 때만 모임 종료, 한 번 더 중 택1
        if (isLeadered) {
            // 모임종료 버튼
            Button(
                onClick = {
                    bookingViewModel.patchBookingEnd(meetingId)
                    val toastTop = Toast.makeText(context, "모임이 종료되었습니다.", Toast.LENGTH_LONG)
                    toastTop.setGravity(Gravity.TOP, 0, 0)
                    toastTop.show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("모임 종료하기")
            }
            // 한번 더 하기 버튼
            Button(
                onClick = {
//                    bookingViewModel.patchBookingEnd(meetingId)
                    val toastTop = Toast.makeText(context, "한 번 더하기(구현 중).", Toast.LENGTH_LONG)
                    toastTop.setGravity(Gravity.TOP, 0, 0)
                    toastTop.show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("한번 더하기 (아직 구현 중)")
            }
        }
    }
}