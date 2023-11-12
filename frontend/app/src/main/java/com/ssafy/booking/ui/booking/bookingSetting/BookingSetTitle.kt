package com.ssafy.booking.ui.booking.bookingSetting

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Text
import coil.compose.rememberImagePainter
import com.ssafy.booking.viewmodel.BookingViewModel
import com.ssafy.booking.viewmodel.LocationViewModel
import com.ssafy.domain.model.booking.BookingDetail

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetTitle() {
    val viewModel: BookingViewModel = hiltViewModel()
    val bookingDetailResponse by viewModel.getBookingDetailResponse.observeAsState()

    LaunchedEffect(Unit) {
        Log.d("test1234", "bookingDetailResponse: $bookingDetailResponse")
    }
    Column{
        bookingDetailResponse?.body()?.coverImage?.let { imageUrl ->
            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = "모임 이미지",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
        bookingDetailResponse?.body()?.let {
               Log.d("SetTitle", "현재 제목: ${viewModel.title.value}")
            OutlinedTextField(

                value = bookingDetailResponse?.body()?.bookTitle ?: "",
                onValueChange = { newValue ->
                    viewModel.title.value = newValue  // 먼저 값 업데이트
                    Log.d("SetTitle", "현재 제목: ${viewModel.title.value}") // 그 후 로그 출력
                },
                label = { Text("제목") }
            )
        }
        Spacer(modifier = Modifier.height(16.dp)) // 간격 추가
        bookingDetailResponse?.body()?.let {
            OutlinedTextField(
                value = bookingDetailResponse?.body()?.description ?: "",
                onValueChange = { viewModel.description.value = it },
                label = { Text("설명") }
            )
        }
    }
    SetTitleBottomButton()

}





@Composable
fun SetTitleBottomButton() {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Button(
            onClick = { /* 버튼 클릭 시 실행할 동작 */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(3.dp) // 사각형 모양
        ) {
            androidx.compose.material3.Text("버튼 텍스트", style = MaterialTheme.typography.bodyMedium)
        }
    }
}