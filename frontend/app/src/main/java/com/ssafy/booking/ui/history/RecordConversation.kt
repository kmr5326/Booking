package com.ssafy.booking.ui.history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import com.ssafy.booking.viewmodel.HistoryViewModel
import com.ssafy.booking.viewmodel.PlayerViewModel
import com.ssafy.domain.model.ChatRoom
import com.ssafy.domain.model.history.Segment
import com.ssafy.domain.model.history.SttResponseDto


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordDetail(
    meetinginfoId: String?
) {
    val playerViewModel: PlayerViewModel = hiltViewModel()
    val historyViewModel: HistoryViewModel = hiltViewModel()

    if (meetinginfoId != null) {
        historyViewModel.loadTransaction(meetinginfoId.toLong())
    }

    val speakToTextInfo = historyViewModel.SpeakToTextInfo.observeAsState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .padding(32.dp)
    ) {
        STTList(historyViewModel, playerViewModel, speakToTextInfo)
    }
}

@Composable
fun STTList(
    historyViewModel: HistoryViewModel,
    playerViewModel: PlayerViewModel,
    speakToTextInfo: SttResponseDto?
) {

    LazyColumn {
        speakToTextInfo?.segments?.let { segments ->
            itemsIndexed(segments) { index, segment ->
                val previousSpeaker = if (index > 0) segments[index - 1].speaker.name else null
                SpeakToTextRow(historyViewModel, playerViewModel, segment, previousSpeaker)
            }
        }
    }
}

@Composable
fun SpeakToTextRow(
    historyViewModel:HistoryViewModel,
    playerViewModel: PlayerViewModel,
    segment: Segment,
    previousSpeaker: String?
) {

    // 다른 상대가 나오면 정렬 반대
    val arrangement = if (segment.speaker.name != previousSpeaker) {
        Arrangement.Start
    } else {
        Arrangement.End
    }

    Row(horizontalArrangement = arrangement) {
        if (segment.speaker.name != null) {
            Text(
                modifier = Modifier
                    .border(BorderStroke(1.dp, Color.Black))
                    .clickable(onClick = { playerViewModel.updateSliderPosition(segment.start.toInt()) }),
                text = playerViewModel.convertMillisToTimeFormat(segment.start.toInt())
            )
            Text(text = segment.speaker.name)
            Text(text = segment.text)
        }
    }
}
