package com.ssafy.booking.ui.booking

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ssafy.booking.R
import com.ssafy.booking.viewmodel.AppViewModel

@Preview
@Composable
fun preview() {
    BookingInfo(
        meetingDate = "2023년 12월 24일",
        meetingPlace = "서울시 강남구",
        participationFee = "10000원"
    )
}
@Composable
fun BookingInfo(
    meetingDate: String?,
    meetingPlace: String?,
    participationFee: String?
) {
    // 맨 위의 사진이랑 책 기본 정보 부분.
     Column {
         Row {
             Image(
                 painter = painterResource(id = R.drawable.book1),
                 contentDescription = "Book image",
                 modifier = Modifier.height(200.dp)
                     .width(140.dp)
             )
             Column {
                 Text(text = "책 제목")
                 Text(text = "책 저자")
                 Text(text = "책 출판사")
                 Text(text = "동해물과 백두산이 마르고 닳도록, 하느님이 보우하사 우리나라 만세")
             }
         }
         // 모임 방제와 모임 소개글. / 해시태그
         Column {
             Text(text = "모임 방제")
             Text(text = "모임 소개글ㅁㅇㄹㅇㅁ너리펀아ㅣ럴ㅇㄴ머ㅣㅇㅁ나런ㅇㅁ런ㅇㅁ런미런ㅁ아런ㅁ이런ㅁㅇㄹㄴㅁ어ㅏㄴㅁ어판ㅁ이ㅓㄹㄴㅁ아ㅣ럼ㄴ아피ㅓㄴㅇ림ㄴ어린ㅁ어파ㅣㅁㄴ얼ㄴㅁㅇ런ㅁ이ㅏㅍㄴㅁ어ㅏㄹㄴㅁㅇ린ㅁ어리ㅏㅁㄴ어라니멒ㅁ아ㅣㅓㄴ아ㅣㅍㄴㅁㄺㅈ댜ㅐㅇ펑미ㅏㅍ어ㅐ")
         }

         // 모임 날짜, 장소, 참가비
         Column {
             if (meetingDate != null) {
                 Text(text = "모임 일정: $meetingDate")
             } else {
                 Text(text = "모임 일정을 아직 정하지 않았습니다.")
             }

             if (meetingPlace != null) {
                 Text(text = "모임 장소: $meetingPlace")
             } else {
                 Text(text = "모임 장소를 아직 정하지 않았습니다.")
             }

             if (participationFee != null) {
                 Text(text = "참가비: $participationFee")
             } else {
                 Text(text = "참가비를 아직 정하지 않았습니다.")
             }
         }

     }
}
