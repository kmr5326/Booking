package com.ssafy.booking.ui.history

import android.media.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.booking.R

@Composable
fun FileUploader() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = {
                    // 클릭 이벤트 처리
                },
                modifier = Modifier
                    .size(100.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_upload_file_24),
                    contentDescription = "녹음 파일 업로드",
                    tint = Color(0xFF00C68E)
                )
            }
            Text(
                text = "녹음 파일 업로드",
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}