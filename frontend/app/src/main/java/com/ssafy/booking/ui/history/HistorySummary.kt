package com.ssafy.booking.ui.history

import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W100
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HistorySummary(
    HistoryId: Int
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .padding(32.dp)
            .background(Color(0xFF00C68E))
    ) {
        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp),
            onClick = {

            }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "독서 모임 요약 수정",
                    tint = Color.White
                )
                Text(
                    text = "수정",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Thin
                )
            }
        }
        Text(
            modifier = Modifier
                .padding(16.dp),
            color = Color.White,
            text = textData
        )
    }
}

val textData = "\"데미안\"의 주인공 신클라이어의 자아 탐색과 내면의 갈등.\n" +
        "\n" +
        "데미안이 실체적 인물인지 아니면 투영인지에 대한 의견. \n" +
        "\n" +
        "'아브라삼의 새' 이야기를 통해 세상에는 단순한 선과 악 이외의 복잡성을 인식."