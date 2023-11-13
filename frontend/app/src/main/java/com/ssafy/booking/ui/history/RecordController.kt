package com.ssafy.booking.ui.history

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import android.Manifest
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import io.grpc.Context

@Composable
fun RecordController(

) {
    val context = LocalContext.current
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                // 권한이 부여되었을 때 녹음 시작 로직
            } else {
                Toast.makeText(context, "녹음 기능을 사용하려면 권한을 수락해야 합니다.", Toast.LENGTH_LONG).show()
            }
        }
    )

    IconButton(
        onClick = {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        },
    ) {
        Icon(
            Icons.Filled.PlayArrow,
            contentDescription = "녹음 시작",
            modifier = Modifier
                .size(100.dp),
            tint = Color(0xFF00C68E)
        )
    }
}

