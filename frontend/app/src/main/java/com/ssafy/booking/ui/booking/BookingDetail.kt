package com.ssafy.booking.ui.booking

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ssafy.booking.ui.common.TabBar


val tabTitles = listOf("모임 정보","참가자","게시판")


@Preview(showBackground = true)
@Composable
fun PreviewMyTabBar() {
    TabBar(
        tabTitles,
        contentForTab = { index ->
            when (index) {
                0 -> Text("모임 정보 내용")
                1 -> Text("참가자 내용")
                2 -> Text("게시판 내용")
            }
        }

    )
}