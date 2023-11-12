package com.ssafy.booking.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun RecordUploader() {
    Box {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(12.dp)
        ) {
            IconButton(
                onClick = {
                    // 클릭 이벤트 처리
                },
            ) {
                Icon(
                    Icons.Filled.ExitToApp,
                    contentDescription = "녹음 파일 업로드",
                    modifier = Modifier
                        .graphicsLayer(rotationZ = 270f)
                        .size(100.dp),
                    tint = Color(0xFF00C68E)
                )
            }
            Text(
                text = "녹음 파일 업로드",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}