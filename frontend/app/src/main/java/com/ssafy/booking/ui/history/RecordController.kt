package com.ssafy.booking.ui.history

import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.ssafy.booking.viewmodel.RecordViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.ssafy.booking.viewmodel.PlayerViewModel
import com.ssafy.booking.viewmodel.RecordingState
import com.ssafy.booking.R


@Composable
fun RecordController(
) {
    val context = LocalContext.current
    val recordViewModel: RecordViewModel = hiltViewModel()

    val recordingState by recordViewModel.recordingState.observeAsState(RecordingState.STOPPED)

    // 권한 요청
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(context, "녹음을 시작합니다.", Toast.LENGTH_SHORT).show()
                recordViewModel.startRecording()
            } else {
                Toast.makeText(context, "녹음 기능을 사용하려면 권한을 수락해야 합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    )


    // 시작
    if (recordingState == RecordingState.STOPPED) {
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
    } else if (recordingState == RecordingState.STARTED || recordingState == RecordingState.RESUMED) {
        IconButton(
            onClick = {
                recordViewModel.pauseRecording()
                Toast.makeText(context, "녹음을 정지합니다.", Toast.LENGTH_SHORT).show()
            },
        ) {
            Icon(
                Icons.Filled.Menu,
                contentDescription = "녹음 중지",
                modifier = Modifier
                    .size(100.dp),
                tint = Color(0xFF00C68E)
            )
        }
    } else if (recordingState == RecordingState.PAUSED) {
        IconButton(
            onClick = {
                Log.d("RECORD_STATE", "$recordingState")
                recordViewModel.resumeRecording()
                Toast.makeText(context, "녹음을 재개합니다.", Toast.LENGTH_SHORT).show()
                Log.d("RECORD_STATE", "$recordingState")
            },
        ) {
            Icon(
                Icons.Filled.PlayArrow,
                contentDescription = "녹음 재개",
                modifier = Modifier
                    .size(100.dp),
                tint = Color(0xFF00C68E)
            )
        }
        IconButton(
            onClick = {
                recordViewModel.stopRecording()
                Toast.makeText(context, "녹음을 저장합니다.", Toast.LENGTH_SHORT).show()
            },
        ) {
            Icon(
                Icons.Filled.ShoppingCart,
                contentDescription = "녹음 저장",
                modifier = Modifier
                    .size(100.dp),
                tint = Color(0xFF00C68E)
            )
        }
    }
}