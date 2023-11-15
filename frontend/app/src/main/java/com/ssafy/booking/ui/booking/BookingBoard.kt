package com.ssafy.booking.ui.booking

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BookingBoard( meetingId : Long,
                  memberRole : String,
                  meetingState : String) {
    Text(text = "게시판")
}
