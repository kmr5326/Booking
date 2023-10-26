package com.ssafy.booking.ui.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ssafy.booking.ui.common.BottomNav
import com.ssafy.booking.viewmodel.AppViewModel


@Composable
fun ChatHome(
    navController: NavController,
    appViewModel: AppViewModel
) {
    Temp(navController, appViewModel)
}

@Composable
fun Temp( navController: NavController,
          appViewModel: AppViewModel) {
    Text(
        text = "채팅 페이지",
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black)
    BottomNav(navController, appViewModel)
}