package com.ssafy.booking.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.booking.R
import com.ssafy.booking.ui.AppNavItem
import com.ssafy.booking.ui.LocalNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingPage() {
    val navController = LocalNavigation.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "설정") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "메뉴"
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {paddingValues->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
            ) {
                Text("알림")
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "알림 내역 관리"
                        )
                        Text(text = "알림 내역 관리")
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "공지사항"
                        )
                        Text("공지사항")
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Warning,
                            contentDescription = "신고내역"
                        )
                        Text(text = "신고내역")
                    }
                }
                Text(text = "기타")
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.qr_code_fill0_wght400_grad0_opsz24),
                            contentDescription = "qr코드"
                        )
                        Text(text = "앱 추천 QR 코드")
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.timeline_fill0_wght400_grad0_opsz24),
                            contentDescription = "현재 버전"
                        )
                        Text(text = "현재 버전")
                        Spacer(modifier = Modifier.padding(horizontal = 70.dp))
                        Text(text = "v1.0.1", color = Color.Gray)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "서비스 이용 약관"
                        )
                        Text(text = "서비스 이용 약관")
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "개인정보 처리방침"
                        )
                        Text(text = "개인정보 처리방침")
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "기타 문의사항은 lovesay00@gmail.com 으로 보내주세요.", color=Color.Gray, fontSize = 10.sp)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {  },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(text = "로그아웃", color = Color.Gray)
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {  },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(text = "회원 탈퇴", color = Color.Red)
                    }
                }
            }

        }
    }
}