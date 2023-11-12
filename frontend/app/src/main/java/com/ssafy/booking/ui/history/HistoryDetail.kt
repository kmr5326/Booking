package com.ssafy.booking.ui.history

import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ssafy.booking.R
import com.ssafy.booking.ui.AppNavItem
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.ui.common.BottomNav
import com.ssafy.booking.ui.common.HorizontalDivider
import com.ssafy.booking.ui.common.TabBar
import com.ssafy.booking.ui.common.TopBar
import com.ssafy.booking.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetail(

) {
    val navController = LocalNavigation.current
    val appViewModel: AppViewModel = hiltViewModel()
    val listState = rememberLazyListState()

    Scaffold(topBar = {
        TopBar("독서모임1")
    }, bottomBar = {
        BottomNav(navController, appViewModel)
    }, modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column {
                BookInfo()
                HorizontalDivider(color = Color.Black)
                RecordList(navController, listState)
            }
        }
    }
}

@Composable
fun BookInfo() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.book2),
            contentDescription = "Book Image",
            modifier = Modifier
                .size(150.dp, 200.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Column {
            Text(text = "인간실격", fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "다자이 오사무 지음 / 김춘미 번역", fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "출간 연도 : 1947년", fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "총 페이지 수 : 191p", fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "자살 미수와 약물 중독, 39세의 젊은 나이에 자살로 생을 마감한 다자이 오사무의 작품 <인간 실격>이 출간됐다. 오직 순수함만을 갈망하는 한 젊은이가 파멸해가는 과정이 그려진다. 뉴욕 타임스는 '인간의 나약함을 드러내는 데 있어 다자이보다 뛰어난 작가는 드물다'고 평했다.작품은 '나'라는 화자가..",
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun RecordList(
    navController : NavController,
    listState: LazyListState
) {
    Box {
        LazyColumn(
            state = listState,
        ) {
            itemsIndexed(recordList) { index, record ->
                RecorItem(navController, record) {}
                Divider()
            }
        }
    }
}

@Composable
fun RecorItem(
    navController: NavController,
    record: RecordData,
    onRowClick : (RecordData) -> Unit
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = {
                onRowClick(record)
                navController.navigate(AppNavItem.HistoryRecord.route)
            })
    ) {
        Column {
            Text(text = record.title, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = record.leaderId, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = record.meetingTime, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = record.meetingPlace, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = record.meetingPay, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

data class RecordData(
    val title: String,
    val leaderId: String,
    val meetingTime: String,
    val meetingPlace: String,
    val meetingPay: String
)

val recordList = listOf(
    RecordData("녹음 1", "사용자1", "2023.10.17 PM 3:00", "광주광역시 광산구 장덕동", "5,000원"),
    RecordData("녹음 2", "사용자3", "2023.10.16 PM 3:00", "광주광역시 광산구 수완동", "7,000원"),
    RecordData("녹음 3", "사용자5", "2023.10.15 PM 3:00", "광주광역시 광산구 하남동", "8,000원"),
    RecordData("녹음 4", "사용자7", "2023.10.14 PM 3:00", "광주광역시 광산구 우산동", "9,000원"),
    RecordData("녹음 5", "사용자9", "2023.10.14 PM 1:00", "광주광역시 광산구 우산동", "10,000원"),

    )
