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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row {
            val imagePainter = if (bookingDetail?.coverImage != null) {
                rememberImagePainter(
                    data = bookingDetail?.coverImage,
                    builder = {
                        crossfade(true)
                    }
                )
            } else {
                painterResource(id = R.drawable.main1) // 기본 이미지
            }

            Image(
                painter = imagePainter,
                contentDescription = "Book Image",
                modifier = Modifier
                    .size(80.dp, 100.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
            Column {
                Text(text = "책 제목 : ${bookingDetail?.bookTitle.orEmpty()}")
                Text(text = "책 저자 : ${bookingDetail?.bookAuthor.orEmpty()}")
                Text(
                    text = "책 내용 : ${bookingDetail?.bookContent.orEmpty()}",
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Column {
            Text(text = "모임 제목 : ${bookingDetail?.meetingTitle.orEmpty()}")
            Text(text = "모임 소개글 : ${bookingDetail?.description.orEmpty()}")
            Text(text = "모임 최대 인원 : ${bookingDetail?.maxParticipants ?: "정보 없음"}")

            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                bookingDetail?.hashtagList?.forEach { hashtag ->
                    HashtagChip(tag = hashtag.content) // 해시태그 칩 표시
                } ?: Text(text = "해시태그 없음")
            }

            val meetingInfo = bookingDetail?.meetingInfoList?.lastOrNull()
            Text(text = "모임 일정")
            Text(text = "${meetingInfo?.date ?: "모임 일정이 아직 정해지지 않았습니다."}")

            Text(text = "참가비")
            Text(text = "${meetingInfo?.fee ?: "아직 정해지지 않았습니다."}")
            Text(text = "모임 장소")
            Text(text = " ${meetingInfo?.location ?: "아직 정해지지 않았습니다."}")
        }
    }

    }

