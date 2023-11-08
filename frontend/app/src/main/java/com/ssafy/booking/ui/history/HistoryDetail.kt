package com.ssafy.booking.ui.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ssafy.booking.R
import com.ssafy.booking.ui.common.BottomNav
import com.ssafy.booking.ui.common.HorizontalDivider
import com.ssafy.booking.ui.common.TabBar
import com.ssafy.booking.ui.common.TopBar
import com.ssafy.booking.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetail(
    navController: NavController, appViewModel: AppViewModel
) {
    Scaffold(topBar = {
        TopBar("독서모임1")
    }, bottomBar = {
        BottomNav(navController, appViewModel)
    }, modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxHeight()
                .padding(paddingValues)
        ) {
            Column {
                HistoryItem()
                VoiceRecordController()
                HorizontalDivider()
                val tempPk = 1
                TabBar(
                    listOf("녹음 기록 분석", "독서 모임 요약"),
                    contentForTab = { index ->
                        when (index) {
                            0 -> HistoryRecord()
                            1 -> HistorySummary(tempPk)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun HistoryItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.book1),
            contentDescription = "Book Image",
            modifier = Modifier
                .size(80.dp, 100.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "사용자1",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "2023.10.17 PM 3:00",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "광주광역시 광산구 수완동",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "5,000원",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
