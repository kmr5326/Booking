package com.ssafy.booking.ui.booking

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.ssafy.booking.R
import com.ssafy.booking.viewmodel.BookingViewModel
import com.ssafy.domain.model.booking.BookingDetail

@Composable
fun BookingInfo(
    meetingId : Long,
)

{
    // 뷰모델 연결

    val bookingViewModel: BookingViewModel = hiltViewModel()
    val getBookingDetailResponse by bookingViewModel.getBookingDetailResponse.observeAsState()
    var bookingDetail by remember { mutableStateOf<BookingDetail?>(null) }
    LaunchedEffect(Unit) {
        bookingViewModel.getBookingDetail(meetingId)
    }

    LaunchedEffect(getBookingDetailResponse) {
        getBookingDetailResponse?.body()?.let { response ->
            Log.d("test334", "$response")
            bookingDetail = response // 상태 업데이트
        }
    }
    // 맨 위의 사진이랑 책 기본 정보 부분.
    Column  (modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()))

    {
        Row {
            Image(
                painter = rememberImagePainter(
                    data = bookingDetail?.coverImage,
                    builder = {
                        crossfade(true) // 이미지가 로딩될 때 페이드인 효과 적용
                    }
                ),
                contentDescription = "Book Image",
                modifier = Modifier
                    .size(80.dp, 100.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop // 이미지의 비율 유지하면서 영역 채우기
            )
            Column {
                Text(text = "책 제목 : ${bookingDetail?.bookTitle}")
                Text(text = "책 저자 : ${bookingDetail?.bookAuthor}")
                Text(
                    text = "책 내용 : ${bookingDetail?.bookContent.orEmpty()}", // 책 내용 공백일 때 예외처리
                    maxLines = 3, // 최대 3줄까지 표시
                    overflow = TextOverflow.Ellipsis // 내용이 넘치면 말줄임표로 표시
                )
            }
        }
        // 모임 방제와 모임 소개글. / 해시태그
        Column {
            Text(text = "모임 방제 : ${bookingDetail?.meetingTitle}")
            Text(text = "모임 소개글 : ${bookingDetail?.description}")

            Text(text = "최대 인원 : ${bookingDetail?.maxParticipants}")
            // 해시태그
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                bookingDetail?.hashtagList?.let { hashtags ->
                    for (hashtag in hashtags) {
                        HashtagChip(tag = hashtag.content) // 각 해시태그에 대한 칩 생성
                    }
                } ?: run {
                    // hashtagList가 null일 경우 처리할 로직

                }
            }

            // 모임 날짜, 시간, 장소
            Column {

            }
            // 맨 마지막 요소만 가져오기.
                Text(text = "모임 날짜,시간 : ${bookingDetail?.meetingInfoList?.lastOrNull()?.date}")

                Text(text = "모임비 : ${bookingDetail?.meetingInfoList?.lastOrNull()?.fee}")
                Text(text = "모임 장소 : ${bookingDetail?.meetingInfoList?.lastOrNull()?.location}")
        }

    }
}
