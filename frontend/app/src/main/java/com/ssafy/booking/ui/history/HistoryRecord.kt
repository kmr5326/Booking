package com.ssafy.booking.ui.history

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ssafy.booking.di.App
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.ui.common.BottomNav
import com.ssafy.booking.ui.common.TabBar
import com.ssafy.booking.ui.common.TopBar
import com.ssafy.booking.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryRecord(
    meetingId: String?,
    meetinginfoId: String?
) {
    val navController = LocalNavigation.current
    val appViewModel: AppViewModel = hiltViewModel()

    // 자신의 정보 가져오기
    val myPk = App.prefs.getMemberPk()
    // 미팅 정보 가져오기 ( 팀장 정보 가져오기 )
    val meetingId = App.prefs.getMeetingId()
    val meetingLeaderId = App.prefs.getLeaderId()
    // 녹음 파일 가져오기 ( File || false )

    Log.d("TT_TEST", "myPk ${myPk}")
    Log.d("TT_TEST", "meetingId ${meetingId}")
    Log.d("TT_TEST", "meetingLeaderId ${meetingLeaderId}")
    // 없으면 업로드 보여주기
    // 있으면 재생 컨트롤러


    Scaffold(topBar = {
        TopBar("${meetingId}의 ${meetinginfoId}번째 모임")
    }, bottomBar = {
        BottomNav(navController, appViewModel)
    }, modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {

//----------------------------------------------------------------------
//                  if 녹음이 없다면
                TabBar(
                    listOf("녹음 파일 업로드", "녹음기"),
                    contentForTab = { index ->
                        when (index) {
                            0 -> FileUploader(meetinginfoId) // 방장이면 업로드 가능, 사용자는 '아직 등록된 녹음이 없습니다.'
                            1 -> RecordController()
                        }
                    }
                )


            }
//                  if 방장이면 리코드 컨트롤러 보임
//                  else if 방장이 아니면 '등록된 녹음이 없습니다.'

//----------------------------------------------------------------------
//                  else if 녹음이 있다면
//                  PlayerController()
//                }
//                val tempPk = 1
//                TabBar(
//                    listOf("녹음 기록 분석", "독서 모임 요약"),
//                    contentForTab = { index ->
//                        when (index) {
//                            0 -> RecordDetail()
//                            1 -> RecordSummary(tempPk)
//                        }
//                    }
//                )
//----------------------------------------------------------------------

        }
    }
}