package com.ssafy.booking.ui.booking

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.ssafy.booking.R
import com.ssafy.booking.viewmodel.AppViewModel

@Preview
@Composable
fun preview() {
}
@Composable
fun BookingInfo(
) {
    Box{
        Column{

        }
    }
}

@Composable
private fun BookInfo() {
    Box {
        Row {
            Image(
                painter = painterResource(id = R.drawable.book1),
                contentDescription = "Book image"
            )
            Column {
                Text(text = "책 제목")
                Text(text = "책 저자")
                Text(text = "책 출판사")
                Text(text = "동해물과 백두산이 마르고 닳도록, 하느님이 보우하사 우리나라 만세")
            }
        }
    }
}

