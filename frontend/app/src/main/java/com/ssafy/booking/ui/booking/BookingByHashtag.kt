package com.ssafy.booking.ui.booking

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ssafy.booking.di.App
import com.ssafy.booking.ui.AppNavItem
import com.ssafy.booking.viewmodel.BookingViewModel
import com.ssafy.domain.model.DeviceToken
import com.ssafy.domain.model.booking.BookingAll

@Composable
fun BookingByHashtag(navController: NavController,hashtagId: Long){
    var hashtagTitle by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        hashtagTitle = App.prefs.getSelectedHashtagTitle()?:""
    }
    Text(text = "#$hashtagTitle")
    BookingListByHashtag(navController,hashtagId)
}
@Composable
fun BookingListByHashtag(navController: NavController,hashtagId: Long) {
    val bookingViewModel: BookingViewModel = hiltViewModel()
//    val hashtagId = App.prefs.getSelectedHashtagId()
    val hashtagState by bookingViewModel.getBookingByHashtagResponse.observeAsState()
    // LaunchedEffect를 사용하여 해당 해시태그 ID로 API 호출
    LaunchedEffect(hashtagId) {
        hashtagId?.let {
            bookingViewModel.getBookingByHashtag(it)
        }
    }

    // LazyColumn에서 검색 결과를 표시
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        // 해시태그 기반의 검색 결과가 있는지 확인하고 items 내에서 반복
        hashtagState?.body()?.let { results ->
            if (results.isNotEmpty()) {

                items(results) { booking ->
                    BookItem(booking = booking, navController)
                }
            } else {
                item {
                    Text("해당 해시태그에 대한 검색 결과가 없습니다.")
                }
            }
        } ?: item {
            // 검색 결과가 아직 없는 경우 (데이터 로딩 중)
            Text("검색 결과를 불러오는 중...")
//            CircularProgressIndicator()
        }
    }
}



