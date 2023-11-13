package com.ssafy.booking.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KakaoPayReadyScreen(
    firstAmount : String
) {
    var amount by remember { mutableStateOf(TextFieldValue(firstAmount)) }

    Scaffold(
        topBar = { BackTopBar(title = "카카오페이 결제 페이지") },
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "충전 금액")
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = amount,
                onValueChange = { newAmount->
                    amount = newAmount
                },
                placeholder = { Text("충전 금액") },
                maxLines = 1, // 최대 6줄 입력 가능
//                modifier = Modifier.height(192.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {

                }
            ) {
                Text(text = "충전 요청")
            }
        }
    }
}