package com.ssafy.booking.ui.history

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
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.ui.common.BottomNav
import com.ssafy.booking.ui.common.TabBar
import com.ssafy.booking.ui.common.TopBar
import com.ssafy.booking.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryRecord(

) {
    val navController = LocalNavigation.current
    val appViewModel: AppViewModel = hiltViewModel()

    Scaffold(topBar = {
        TopBar("녹음 1")
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
                Row {
                    RecordController()
                    RecordUploader()
                }
                val tempPk = 1
                TabBar(
                    listOf("녹음 기록 분석", "독서 모임 요약"),
                    contentForTab = { index ->
                        when (index) {
                            0 -> RecordDetail()
                            1 -> RecordSummary(tempPk)
                        }
                    }
                )


            }
        }
    }
}

@Composable
fun MeetingInfo() {

}
