package com.ssafy.booking.ui.history

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
import androidx.compose.foundation.lazy.items
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ssafy.booking.R
import com.ssafy.booking.ui.AppNavItem
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.ui.common.BottomNav
import com.ssafy.booking.ui.common.TopBar
import com.ssafy.booking.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryHome(

) {
    val navController = LocalNavigation.current
    val appViewModel: AppViewModel = hiltViewModel()
//    val viewModel : HistoryViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            TopBar("이전 독서 모임")
        },
        bottomBar = {
            BottomNav(navController, appViewModel)
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .fillMaxHeight()
        ) {
            HistoryList(navController, appViewModel)
        }
    }
}

@Composable
fun HistoryList(navController: NavController, appViewModel: AppViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .padding(horizontal = 8.dp, vertical = 15.dp)
    ) {
        items(historyTempList) { history ->
            HistoryItem(history) {}
        }
    }
}

@Composable
fun HistoryItem(
    historyItem: HistoryData,
    onRowClick: (HistoryData) -> Unit
) {
    val navController = LocalNavigation.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable(onClick = {
                onRowClick(historyItem)
                navController.navigate(AppNavItem.HistoryDetail.route)
            })
    ) {
        Image(
            painter = painterResource(id = historyItem.imageResId),
            contentDescription = "Book Image",
            modifier = Modifier
                .size(80.dp, 100.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = historyItem.title, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = historyItem.leaderId,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = historyItem.meetingTime,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = historyItem.meetingPlace,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = historyItem.meetingPay,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

data class HistoryData(
    val imageResId: Int,
    val title: String,
    val leaderId: String,
    val meetingTime: String,
    val meetingPlace: String,
    val meetingPay: String
)

val historyTempList = listOf(
    HistoryData(R.drawable.book1, "독서모임1", "사용자1", "2023.10.17 PM 3:00", "광주광역시 광산구 수완동", "5,000원"),
    HistoryData(R.drawable.book2, "독서모임2", "사용자2", "2023.10.17 PM 4:00", "광주광역시 광산구 수완동", "5,000원"),
    HistoryData(R.drawable.book5, "독서모임3", "사용자3", "2023.10.17 PM 5:00", "광주광역시 광산구 수완동", "5,000원"),
    HistoryData(R.drawable.book4, "독서모임4", "사용자4", "2023.10.17 PM 6:00", "광주광역시 광산구 수완동", "5,000원"),
    HistoryData(R.drawable.book5, "독서모임5", "사용자5", "2023.10.17 PM 7:00", "광주광역시 광산구 수완동", "5,000원"),
    HistoryData(R.drawable.book1, "독서모임6", "사용자6", "2023.10.17 PM 8:00", "광주광역시 광산구 수완동", "5,000원"),
    HistoryData(R.drawable.book7, "독서모임7", "사용자7", "2023.10.17 PM 9:00", "광주광역시 광산구 수완동", "5,000원")
)
