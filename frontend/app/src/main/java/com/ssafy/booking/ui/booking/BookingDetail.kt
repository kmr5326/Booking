
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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    val getParticipantsResponse by bookingViewModel.getParticipantsResponse.observeAsState()
    val getWaitingListResponse by bookingViewModel.getWaitingListResponse.observeAsState()
    val createBookingSuccess by bookingViewModel.createBookingSuccess.observeAsState()

    val context = LocalContext.current
    val navController = LocalNavigation.current
    
    // 리더인지 아닌지
    var memberRole by remember { mutableStateOf("GUEST") }
    // 모임 진행 상황
    var meetingState by remember { mutableStateOf("PREPAIRING") }
    LaunchedEffect(Unit) {
        bookingViewModel.getBookingDetail(meetingId)
        bookingViewModel.getParticipants(meetingId)
        bookingViewModel.getWaitingList(meetingId)
    }
    // 3개 중 하나라도 바뀌면 리렌더링
    LaunchedEffect(getBookingDetailResponse, getParticipantsResponse, getWaitingListResponse,createBookingSuccess) {
        meetingState = getBookingDetailResponse?.body()?.meetingState ?: "PREPARING"
        // 리더인지 확인
        val memberPk = App.prefs.getMemberPk().toInt()
        val leaderId = getBookingDetailResponse?.body()?.leaderId
        if (memberPk == leaderId) {
            memberRole = "LEADER"
        } else {
            // 참여자 명단 확인
            val participants = getParticipantsResponse?.body()
            val isParticipant = participants?.any { it.memberPk == memberPk }

            // 대기자 명단 확인
            val waitingList = getWaitingListResponse?.body()
            val isWaiting = waitingList?.any { it.memberPk == memberPk }

            when {
                isParticipant == true -> memberRole = "PARTICIPANT" // 참여자
                isWaiting == true -> memberRole = "WAITING" // 대기자
                else -> memberRole = "GUEST" // 모두 해당되지 않으면 게스트
            }
        }
    }

    Scaffold { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                BackTopBar(title = "모임 상세")
            }
            item {
                TabBar(
                    tabTitles,
                    contentForTab = { index ->
                        when (index) {
                            0 -> BookingInfo(
                                meetingId = meetingId,
                                memberRole = memberRole,
                                meetingState = meetingState,
                            )

                            1 -> BookingParticipants(
                                meetingId = meetingId,
                                memberRole = memberRole,
                                meetingState = meetingState,
                            )

                            2 -> BookingBoard(
                                meetingId = meetingId,
                                memberRole = memberRole,
                                meetingState = meetingState,
                            )
                        }
                    }
                )
            }

            item {
                BottomBar(
                    memberRole,
                    meetingState,
                    meetingId,
                    bookingViewModel,
                    context,
                    navController
                )
            }}
        }
    }



@Composable
fun BottomBar(memberRole:String,meetingState:String,meetingId:Long,bookingViewModel: BookingViewModel,context:Context ,navController:NavController){
    when (meetingState) {
        "PREPARING" -> preparingBottomBar(memberRole,meetingId,bookingViewModel,context,navController)
        "ONGOING" -> ongoingBottomBar(memberRole,meetingId,bookingViewModel,context,navController)
        "FINISH" -> finishBottomBar(memberRole,meetingId)
    }
}

// 상태가 PREPAIRING 일 때,
@Composable
fun preparingBottomBar(memberRole:String,meetingId:Long,bookingViewModel: BookingViewModel,context:Context ,navController:NavController) {
    when (memberRole) {
        "LEADER" -> {
            ModifyButton(navController,meetingId,bookingViewModel,context)
            StartButton(navController,meetingId,bookingViewModel,context)
        }
        "PARTICIPANT" ->
            ExitButton(navController,meetingId,bookingViewModel,context)
        "WAITING" ->
            WaitingButton()
        else -> {
            // 게스트
            JoinRequestButton(navController = navController,meetingId,bookingViewModel,context)
        }
    }
}

// 상태가 ONGOING 일 때,
@Composable
fun ongoingBottomBar(memberRole:String,meetingId:Long,bookingViewModel: BookingViewModel,context:Context ,navController:NavController){
    when (memberRole) {
        "LEADER" -> {
            EndButton(navController,meetingId,bookingViewModel,context) // 종료
            RestartButton(navController,meetingId,bookingViewModel,context) // 한 번 더
            AttendCheckButton(navController,meetingId,bookingViewModel,context)
            PayRequestButton(navController,meetingId,bookingViewModel,context)
        }
        "PARTICIPANT" -> {
            AttendCheckButton(navController,meetingId,bookingViewModel,context)
            PayRequestButton(navController,meetingId,bookingViewModel,context)
        }
        else -> {
            // 게스트
            AlreadyOngoingButton()
        }
    }
}
// 상태가 FINISH 일 때,
@Composable
fun finishBottomBar(memberRole:String,meetingId:Long){
    AlreadyEndButton()
}

// 모임 가입 신청 버튼
@Composable
fun JoinRequestButton(navController: NavController,meetingId:Long,bookingViewModel: BookingViewModel,context: Context){
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

}
// 모임 확정 버튼
@Composable

fun StartButton(
    navController: NavController,
    meetingId: Long,
    bookingViewModel: BookingViewModel,
    context: Context
){
    Button(onClick = {
        navController.navigate(AppNavItem.BookingSetLocation.route)
    }) {
        Text(text = "모임 확정")
    }
}
// 모임 수정 버튼
@Composable
fun ModifyButton(
    navController: NavController,
    meetingId: Long,
    bookingViewModel: BookingViewModel,
    context: Context
){
    Button(onClick = {
        navController.navigate(AppNavItem.BookingSetTitle.route)
    }) {
        Text(text = "모임 수정")
    }
}

// 모임 나가기 버튼
@Composable
fun ExitButton(
    navController: NavController,
    meetingId: Long,
    bookingViewModel: BookingViewModel,
    context: Context
){
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
// 모임 출첵 버튼
@Composable
fun AttendCheckButton(
    navController : NavController,
    meetingId : Long,
    bookingViewModel : BookingViewModel,
    context : Context
){
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
}
// 모임 종료 버튼
@Composable
fun EndButton(
    navController : NavController,
    meetingId : Long,
    bookingViewModel : BookingViewModel,
    context : Context
){
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

}
// 모임 다시 시작(한 번 더 ) 버튼
@Composable
fun RestartButton(
    navController : NavController,
    meetingId : Long,
    bookingViewModel : BookingViewModel,
    context : Context
) {
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

// 참가비 결제 버튼
@Composable
fun PayRequestButton(
    navController : NavController,
    meetingId : Long,
    bookingViewModel : BookingViewModel,
    context : Context
){
    Button(onClick = { /*TODO*/ }) {
        Text(text="참가비 결제하기")
    }
}

// 참가신청 승인 대기 버튼 ( 기다리는 중..)
@Composable
fun WaitingButton(
) {
    Button(onClick = { /*TODO*/ }) {
        Text(text = "가입 승인 대기 중..")
    }
}
// 이미 참가한 사람에게 보여지는 버튼
@Composable
fun AlreadyJoinButton() {
    Button(onClick = { /*TODO*/ }) {
        Text(text = "이미 참가한 모임입니다.")
    }
}

// 이미 진행중인 모임에 대해 보여지는 버튼
@Composable
fun AlreadyOngoingButton() {
    Button(onClick = { /*TODO*/ }) {
        Text(text = "이미 진행중인 모임입니다.")
    }
}

// 이미 종료된 모임에 대해 보여지는 버튼
@Composable
fun AlreadyEndButton(){
    Button(onClick = { /*TODO*/ }) {
        Text(text = "이미 종료된 모임입니다.")
    }

}