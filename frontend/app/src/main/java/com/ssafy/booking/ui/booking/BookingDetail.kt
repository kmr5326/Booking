package com.ssafy.booking.ui.booking

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ssafy.booking.ui.common.BackTopBar
import com.ssafy.booking.ui.common.TabBar

val tabTitles = listOf("모임 정보", "참가자", "게시판")


@Composable
fun BookingDetail(meetingId:Long) {
    Column  {

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
    }

}