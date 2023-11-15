package com.ssafy.booking.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ssafy.booking.viewmodel.HistoryViewModel

@Composable
fun RecordSummary(
) {
    val historyViewModel: HistoryViewModel = hiltViewModel()
    val summary by historyViewModel.SummaryInfo.observeAsState("기본값")
    val transactionId by historyViewModel.TransactionId.observeAsState("")

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .padding(32.dp)
            .background(Color(0xFF00C68E))
    ) {
        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp),
            onClick = {

            }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "독서 모임 요약 수정",
                    tint = Color.White
                )
                Text(
                    text = "수정",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Thin
                )
            }
        }
        Text(
            modifier = Modifier
                .padding(16.dp),
            color = Color.White,
            text = summary
        )
    }
}
