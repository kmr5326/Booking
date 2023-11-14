package com.ssafy.booking.ui.history

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
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
import com.ssafy.booking.viewmodel.PlayerViewModel
import androidx.compose.ui.platform.LocalContext
import com.ssafy.booking.R


@Composable
fun PlayerController(
) {
    val context = LocalContext.current
    val playerViewModel : PlayerViewModel = hiltViewModel()
    Box(
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(12.dp)
        ) {
            IconButton(
                onClick = {
                    val resourceUri = Uri.parse("android.resource://${context.packageName}/${R.raw.crowd_talking}")
                    playerViewModel.playAudio(context, resourceUri)
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
            SeekBar(playerViewModel)
        }
    }
}

@Composable
fun SeekBar(
    playerViewModel: PlayerViewModel
) {
    val sliderPosition by playerViewModel.sliderPosition.observeAsState(0)
    val totalDuration by playerViewModel.totalDuration.observeAsState(0)


    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Slider(
            value = sliderPosition.toFloat(),
            onValueChange = { newPosition ->
                playerViewModel.updateSliderPosition(newPosition.toInt())
            },
            valueRange = 0f..totalDuration.toFloat(),
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF00C68E),
                activeTrackColor = Color(0xFF00C68E),
                inactiveTrackColor = Color(0xFFDAF6EE),
            ),
            modifier = Modifier
                .size(100.dp)
        )
        Text(
            text = "${playerViewModel.convertMillisToTimeFormat(sliderPosition)} / ${playerViewModel.convertMillisToTimeFormat(totalDuration)}"
        )
    }
}



