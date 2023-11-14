package com.ssafy.booking.ui.history

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.ssafy.booking.viewmodel.UploaderViewModel
import okhttp3.Request

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryRecord(
    meetingId: String?,
    meetinginfoId: String?,
) {
    val navController = LocalNavigation.current
    val appViewModel: AppViewModel = hiltViewModel()
    val uploaderViewModel: UploaderViewModel = hiltViewModel()

    val myPk = App.prefs.getMemberPk()
    val meetingId = App.prefs.getMeetingId()
    val meetingLeaderId = App.prefs.getLeaderId()
    var isLeader by remember { mutableStateOf(false) }
    isLeader = myPk.toInt() == meetingLeaderId

    var isLoadRecord by remember { mutableStateOf(false) }
    val responseState by uploaderViewModel.naverCloudGetResponse.observeAsState()
    responseState?.let { response ->
        if (response.isSuccessful) {
            isLoadRecord = true
            Log.d("STT_TEST", "녹음파일을 불러왔습니다!")
        } else {
            Log.d("STT_TEST", "녹음파일을 불러오지 못했습니다.")
        }
    }

    LaunchedEffect(Unit) {
        uploaderViewModel.GetToNaverCloud(meetinginfoId)
    }

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
                if (isLoadRecord == false) {
                    TabBar(
                        listOf("녹음 파일 업로드", "녹음기"),
                        contentForTab = { index ->
                            when (index) {
                                0 -> FileUploader(meetinginfoId, isLeader, meetingId) // 방장이면 업로드 가능, 사용자는 '아직 등록된 녹음이 없습니다.'
                                1 -> RecordController()
                            }
                        }
                    )
                } else if(isLoadRecord == true) {
                    PlayerController(meetinginfoId)
                    TabBar(
                        listOf("녹음 기록 분석", "녹음 모임 요약"),
                        contentForTab = { index ->
                            when (index) {
                                0 -> RecordDetail(meetinginfoId)
                                1 -> RecordSummary()
                            }
                        }
                    )
                }
            }
        }
    }
}