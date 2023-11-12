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
import androidx.hilt.navigation.compose.hiltViewModel
import com.ssafy.booking.viewmodel.RecordViewModel
import androidx.compose.ui.platform.LocalContext
import com.ssafy.booking.R


@Composable
fun RecordController(
) {
    val context = LocalContext.current
    val recordViewModel : RecordViewModel = hiltViewModel()
    Box {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(12.dp)
        ) {
            IconButton(
                onClick = {
                    val resourceUri = Uri.parse("android.resource://${context.packageName}/${R.raw.crowd_talking}")
                    recordViewModel.playAudio(context, resourceUri)
                },
            ) {
                Icon(
                    Icons.Filled.PlayArrow,
                    contentDescription = "재생",
                    modifier = Modifier
                        .size(100.dp),
                    tint = Color(0xFF00C68E)
                )
            }
            SeekBar(recordViewModel)
        }
    }
}

@Composable
fun SeekBar(
    recordViewModel: RecordViewModel
) {
    val sliderPosition by recordViewModel.sliderPosition.observeAsState(0)
    val totalDuration by recordViewModel.totalDuration.observeAsState(0)


    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Slider(
            value = sliderPosition.toFloat(),
            onValueChange = { newPosition ->
                recordViewModel.updateSliderPosition(newPosition.toInt())
            },
            valueRange = 0f..totalDuration.toFloat(), // 녹음 파일의 길이만큼 할 것
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF00C68E), // 원
                activeTrackColor = Color(0xFF00C68E), // 진행
                inactiveTrackColor = Color(0xFFDAF6EE), // 전체
            ),
            modifier = Modifier
                .size(100.dp)
        )
        Text(
            text = "${recordViewModel.convertMillisToTimeFormat(sliderPosition)} / ${recordViewModel.convertMillisToTimeFormat(totalDuration)}"
        )
    }
}



