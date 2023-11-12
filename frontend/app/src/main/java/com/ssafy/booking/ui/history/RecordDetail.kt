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
                        .clickable(onClick = { recordViewModel.updateSliderPosition(30000) }),
                    text = "${recordViewModel.convertMillisToTimeFormat(30000)}"
                )
                Text(text = "그래 그리 쉽지는 않겠지")
            }
            Row {
                Text(
                    modifier = Modifier
                        .border(BorderStroke(1.dp, Color.Black))
                        .clickable(onClick = { recordViewModel.updateSliderPosition(50000) }),
                    text = "${recordViewModel.convertMillisToTimeFormat(50000)}"
                )
                Text(text = "나를 허락해준 세상이란")
            }
            Row {
                Text(
                    modifier = Modifier
                        .border(BorderStroke(1.dp, Color.Black))
                        .clickable(onClick = { recordViewModel.updateSliderPosition(90000) }),
                    text = "${recordViewModel.convertMillisToTimeFormat(90000)}"
                )
                Text(text = "손쉽게 다가오는")
            }
            Row {
                Text(
                    modifier = Modifier
                        .clickable(onClick = { recordViewModel.updateSliderPosition(120000) }),
                    text = "${recordViewModel.convertMillisToTimeFormat(120000)}"
                )
                Text(text = "편하고도 감미로운 공간이 아냐")
            }
        }
    }


}

