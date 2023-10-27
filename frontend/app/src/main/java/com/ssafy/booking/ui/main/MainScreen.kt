package com.ssafy.booking.ui.main

import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ssafy.booking.ui.common.BottomNav
import com.ssafy.booking.ui.theme.BookingTheme
import com.ssafy.booking.viewmodel.AppViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.ssafy.booking.R

@Composable
fun Main(
    navController: NavController, appViewModel: AppViewModel
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
//                    .align(Alignment.TopStart)  // Align to top-left
                    .size(120.dp, 120.dp)
                    .padding(start = 16.dp)
            )
            BookList()
        }

    }
    // 책 목록 부분

    BottomNav(
        navController, appViewModel
    )  // You might want to adjust the positioning of BottomNav as per your design.
}


////

@Composable
fun BookList() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(bookItemsList) { book ->
            BookItem(book)
        }
    }
}
@Composable
fun BookItem(book: Book) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Image(
            painter = painterResource(id = book.imageResId),
            contentDescription = "Book Image",
            modifier = Modifier
                .size(80.dp, 100.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = book.bookName, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = book.title, maxLines = 2, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.Medium, fontSize=12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = book.content, maxLines = 2, overflow = TextOverflow.Ellipsis, fontSize=10.sp)
        }
    }
}

data class Book(val imageResId: Int, val bookName: String, val title: String, val content:String )

val bookItemsList = listOf(
    Book(R.drawable.book1, "데미안", "데미안에 대해 읽고 같이 토론하실 분","이번 주말까지 읽고, 카페에서 자유롭게 이야기 나누실 분"),
    Book(R.drawable.book2, "인간 실격", "인간 실격 인상 깊게 읽으신 분 이야기 나눠요","세미나식으로 각자 발표 후, Q&A로 하겠습니다."),
    Book(R.drawable.book4, "나미야 잡화점의 기적", "나미야 잡화점에 대해 이야기 나누실 분","자유 토론으로 각자의 의견을 제시하면 됩니다."),
    Book(R.drawable.book7, "불편한 편의점", "불편한 편의점에 대해 이야기 나누실 분","자유 토론으로 각자의 의견을 제시하면 됩니다.")
)




