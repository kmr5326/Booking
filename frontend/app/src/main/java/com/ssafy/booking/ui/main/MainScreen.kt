package com.ssafy.booking.ui.main

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ssafy.booking.ui.common.BottomNav
import com.ssafy.booking.viewmodel.AppViewModel

@Composable
fun Main(
    navController: NavController,
    appViewModel: AppViewModel
) {
    Text(
        text = "북킹",
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black)
    BottomNav(navController, appViewModel)
}


