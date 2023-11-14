package com.ssafy.booking.ui.booking

import androidx.compose.runtime.Composable
import com.ssafy.booking.di.App

@Composable
fun BookingByHashtag() {
    val hashtagId = App.prefs.getSelectedHashtagId()
    val hashtagTitle = App.prefs.getSelectedHashtagTitle()
}
