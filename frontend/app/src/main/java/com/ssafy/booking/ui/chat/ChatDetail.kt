package com.ssafy.booking.ui.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.ssafy.booking.ui.common.BottomNav
import com.ssafy.booking.ui.common.TopBar
import com.ssafy.booking.viewmodel.AppViewModel

@Composable
fun ChatDetail(
    navController: NavController,
    appViewModel: AppViewModel,
) {
    val chatId = navController
        .currentBackStackEntry
        ?.arguments
        ?.getString("chatId")

    Column {
        TopBar(title = "${chatId}번째 채팅방")
        Box(modifier = Modifier.fillMaxSize()) {

        }
    }
}
