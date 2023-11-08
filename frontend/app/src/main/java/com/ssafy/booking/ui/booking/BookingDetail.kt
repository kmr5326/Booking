package com.ssafy.booking.ui.booking

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ssafy.booking.ui.common.TabBar

val tabTitles = listOf("모임 정보", "참가자", "게시판")

@Preview(showBackground = true)
@Composable
fun PreviewMyTabBar() {
    TabBar(
        tabTitles,
        contentForTab = { index ->
            when (index) {
                0 -> BookingInfo(
                    meetingDate = "2023년 12월 24일",
                    meetingPlace = "서울시 강남구",
                    participationFee = "10000원"
                )
                1 -> BookingParticipants()
                2 -> BookingBoard()
            }
        }

    )
}
