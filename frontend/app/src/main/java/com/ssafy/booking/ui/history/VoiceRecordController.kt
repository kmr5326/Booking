package com.ssafy.booking.ui.history

import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun VoiceRecordController(
) {

    // uri
    val url = "https://webaudioapi.com/samples/audio-tag/chrono.mp3"
    val mediaplayer = MediaPlayer()
    mide


    Column(modifier = Modifier
        .fillMaxSize(), 
        verticalArrangement = Arrangement.Center, 
        horizontalAlignment = Alignment.CenterHorizontally) {

        Row {
            IconButton(onClick = { mediaPlayer.start() }) {
                Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "재생")
            }
            IconButton(onClick = { mediaPlayer.pause() }) {
                Icon(imageVector = Icons.Filled.Refresh, contentDescription = "정지")
            }
        }
    }
}

data class RecordData(
    val id: Int, val start: Int, val end: Int, val text: String, val speaker: String
)

val recordTempData = listOf(
    RecordData(0, 3380, 5887, "아침에 뭐 다 지", "A"),
    RecordData(
        1, 6887, 13887, "아침에 오자마자 지도 계속 쳐다보고 있는데 웹에서 제공해 주는 기능과는 달리 안드로이드에서 제공해 주는 api 진짜", "A"
    ),
    RecordData(2, 13887, 17387, "거의 없더라고요 그걸 좀 더 찾아봐야 할 것 같고", "C"),
    RecordData(3, 18387, 21710, "오늘 오후에 사랑님 갈치해야 해서", "A"),
    RecordData(4, 23430, 31097, "하고 모임 생성 어제 안 돼서 현영이 된다. 그러면 이제 이후에 테스트를", "A"),
    RecordData(5, 31097, 33097, "몇 분만 기다리", "B"),
    RecordData(6, 56957, 60790, "하이어 베이스 알", "G"),
)