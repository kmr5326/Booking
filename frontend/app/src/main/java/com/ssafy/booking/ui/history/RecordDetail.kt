package com.ssafy.booking.ui.history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordDetail(

) {
    val recordViewModel: RecordViewModel = hiltViewModel()
    val sliderPosition by recordViewModel.sliderPosition.observeAsState(0)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .padding(32.dp)
    ) {
        Column {
            Row {
                Text(
                    modifier = Modifier
                        .border(BorderStroke(1.dp, Color.Black))
                        .clickable(onClick = { recordViewModel.updateSliderPosition(30) }),
                    text = "30"
                )
                Text(text = "데미안을 읽으면서 가장 인상 깊었던 것은 주인공 신클라이어의 성장 과정이었습니다.")
            }
            Row {
                Text(
                    modifier = Modifier
                        .border(BorderStroke(1.dp, Color.Black))
                        .clickable(onClick = { recordViewModel.updateSliderPosition(50) }),
                    text = "50"
                )
                Text(text = "맞아요. 데미안과의 관계를 통해 신클라이어가 자신의 내면을 깊게 탐색하는 모습이 인상적이었어요.")
            }
            Row {
                Text(
                    modifier = Modifier
                        .border(BorderStroke(1.dp, Color.Black))
                        .clickable(onClick = { recordViewModel.updateSliderPosition(70) }),
                    text = "70"
                )
                Text(text = "데미안이란 인물도 매우 흥미로웠어요. 그는 마치 신클라이어의 또 다른 자아, 혹은 그의 내면의 가이드 같았어요.")
            }
            Row {
                Text(
                    modifier = Modifier
                        .clickable(onClick = { recordViewModel.updateSliderPosition(90) }),
                    text = "90"
                )
                Text(text = "저는 데미안이 신클라이어의 내면의 투영이라고 생각했어요.")
            }
        }
    }


}

