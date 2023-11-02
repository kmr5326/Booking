package com.ssafy.booking.ui.book

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ssafy.booking.R
import com.ssafy.booking.ui.common.BottomNav
import com.ssafy.booking.ui.common.TopBar
import com.ssafy.booking.viewmodel.AppViewModel


@Composable
fun BookHome(
    navController: NavController,
    appViewModel: AppViewModel
) {
    Temp(navController, appViewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Temp( navController: NavController,
          appViewModel: AppViewModel) {
    Scaffold (
        topBar = {
            TopBar("책")
        },
        bottomBar = {
            BottomNav(navController, appViewModel)
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column (
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                modifier = Modifier.padding(20.dp),
                text = "도서 페이지",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.font_color)
            )
        }
    }
}