package com.ssafy.booking.ui.book

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.viewmodel.BookSearchViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetail(isbn: String) {

    val navController = LocalNavigation.current

    val viewModel : BookSearchViewModel = hiltViewModel()

    val getBookSearchByIsbnResponse by viewModel.getBookSearchByIsbnResponse.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getBookSearchByIsbn(isbn)
        Log.d("test","$isbn")
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                 title = { Text(text = "도서") },
                 navigationIcon = {
                     IconButton(onClick = { navController.popBackStack() }) {
                         Icon(
                             imageVector = Icons.Filled.ArrowBack,
                             contentDescription = "뒤로가기"
                         )
                     }
                 },
             )
        },
        modifier = Modifier.fillMaxSize()
    ) {paddingValues ->
        getBookSearchByIsbnResponse?.let {
            val bookDetail = it.body()
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(20.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier.width(180.dp)
                        .padding(10.dp)
                ) {
                    // 북커버 이미지
                    AsyncImage(
                        model = bookDetail!!.coverImage,
                        contentDescription = "북 커버",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(250.dp)
                            .fillMaxWidth())
                }
                // 제목
                Text(text = "${bookDetail!!.title}")
                Spacer(modifier = Modifier.padding(5.dp))
                // 저자
                Text(text = "${bookDetail!!.author}")
                Spacer(modifier = Modifier.padding(5.dp))
                // 출간 연도
                Text(text = "${bookDetail!!.publishDate}")
                Spacer(modifier = Modifier.padding(5.dp))
                // 페이지
                Text(text = "${bookDetail!!.genre}")
                Spacer(modifier = Modifier.padding(10.dp))
                // 내용
                Text(text = "${bookDetail!!.content}")
            }
        } ?: run {
            Text(text = "로딩 중")
        }
    }
}