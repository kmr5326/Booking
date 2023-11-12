package com.ssafy.booking.ui.booking.bookingSetting

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Text
import coil.compose.rememberImagePainter
import com.ssafy.booking.di.App
import com.ssafy.booking.ui.AppNavItem
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.viewmodel.BookingViewModel
import com.ssafy.booking.viewmodel.LocationViewModel
import com.ssafy.data.repository.token.TokenDataSource
import com.ssafy.domain.model.booking.BookingDetail

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetTitle() {
    val viewModel: BookingViewModel = hiltViewModel()
    val bookingDetailResponse by viewModel.getBookingDetailResponse.observeAsState()
    val title = App.prefs.getTitle()
    val description = App.prefs.getDescription()
    val bookImage = App.prefs.getBookImage()
    val bookTitle = App.prefs.getBookTitle()
    val bookAuthor = App.prefs.getBookAuthor()
    val titleState by viewModel.title.observeAsState()
    val descriptionState by viewModel.description.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.title.value = title
        viewModel.description.value = description
    }

    Scaffold(
        bottomBar = {
            SetTitleBottomButton(titleState,descriptionState) // 하단 버튼
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // 책 이미지
            bookImage?.let {
                Column{
                    Text(text = bookTitle ?: "")
                    Text(text = bookAuthor ?: "")
                    Image(
                        painter = rememberImagePainter(bookImage),
                        contentDescription = "모임 이미지",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
            }

            // 제목 입력 필드
            title?.let {
                OutlinedTextField(
                    singleLine = true,
                    value = titleState ?: "",
                    onValueChange = { newValue ->
                        viewModel.title.value = newValue
                    },
                    label = { Text("제목") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 설명 입력 필드
            description?.let {
                OutlinedTextField(
                    maxLines = 10,
                    value = descriptionState ?: "",
                    onValueChange = { newValue ->
                        viewModel.description.value = newValue
                    },
                    label = { Text("모임 소개") }
                )
            }
        }
    }
}





@Composable
fun SetTitleBottomButton(
    titleState: String?,
    descriptionState: String?,
) {
    // 현재 Composable 함수와 연관된 Context 가져오기
    val context = LocalContext.current
    val navController = LocalNavigation.current

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                if (titleState.isNullOrEmpty() || descriptionState.isNullOrEmpty()) {
                    // 제목 또는 내용이 비어있을 경우 Toast 메시지 표시
                    Toast.makeText(context, "제목과 내용을 모두 입력해주세요.", Toast.LENGTH_LONG).show()
                } else {
                    // 모두 입력된 경우 네비게이션
                    navController.navigate(AppNavItem.BookingSetLocation.route)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(3.dp)
        ) {
            Text("버튼 텍스트", style = MaterialTheme.typography.bodyMedium)
        }
    }
}


