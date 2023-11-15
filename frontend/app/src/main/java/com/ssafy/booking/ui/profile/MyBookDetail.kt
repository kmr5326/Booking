package com.ssafy.booking.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.ssafy.booking.R
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.viewmodel.MyBookViewModel
import com.ssafy.data.repository.token.TokenDataSource
import com.ssafy.domain.model.mybook.MyBookListResponse
import com.ssafy.domain.model.mybook.MyBookMemoRegisterRequest
import java.time.LocalDate
import java.time.LocalDate.now


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookDetail(
    isbn: String?
) {
    val navController = LocalNavigation.current
    val viewModel: MyBookViewModel = hiltViewModel()
    val context = LocalContext.current
    val tokenDataSource = TokenDataSource(context)
    val memberPk: Long = tokenDataSource.getMemberPk()

    val myBookDetailResponse by viewModel.myBookDetailResponse.observeAsState()

    var BookDetailState by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        if (memberPk != null && isbn != null) {
            viewModel.getMyBookDetailResponse(memberPk, isbn)
        }
    }

    LaunchedEffect(myBookDetailResponse) {
        myBookDetailResponse?.let {
            if (myBookDetailResponse!!.isSuccessful) {
                // 성공 view 띄우기
                BookDetailState = 1
            } else {
                // 실패 view 띄우기
                BookDetailState = 2
            }
        } ?: run {
            // null 에러 띄우기
            BookDetailState = 2
        }
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
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (BookDetailState == 0) {
                Text(text = "로딩중..")
            } else if (BookDetailState == 1) {
                DetailBookSuccessView(myBookDetailResponse!!.body(), viewModel, memberPk)
            } else {
                DetailBookErrorView()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBookSuccessView(
    myBookDetailResponse: MyBookListResponse?,
    viewModel: MyBookViewModel,
    memberPk: Long
) {
    var memo by remember { mutableStateOf("") }

    AsyncImage(
        model = myBookDetailResponse!!.bookInfo.coverImage,
        contentDescription = "북 커버",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .height(450.dp)
            .fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(14.dp))
    Text(
        text = myBookDetailResponse!!.bookInfo.title,
    )
    Spacer(modifier = Modifier.height(14.dp))
    Text(
        text = myBookDetailResponse!!.bookInfo.author,
    )
    Spacer(modifier = Modifier.height(14.dp))
    Text(
        text = myBookDetailResponse!!.bookInfo.genre,
    )
    Spacer(modifier = Modifier.height(14.dp))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "한줄평")
        Spacer(modifier = Modifier.padding(10.dp))
        OneLineMemos(myBookDetailResponse, memberPk, myBookDetailResponse.bookInfo.isbn)
        OutlinedTextField(
            value = memo, // 이 부분을 뷰모델의 상태로 연결하거나 필요에 따라 변경
            onValueChange = { newValue ->
                memo = newValue
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(top = 4.dp)
                .padding(bottom = 16.dp)
                .height(50.dp),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF12BD7E)
            ),
            textStyle = TextStyle(
//                color = colorResource(id = R.color.font_color),
                fontSize = 14.sp,
                baselineShift = BaselineShift.None
            ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    val result = MyBookMemoRegisterRequest(
                        memberPk = memberPk,
                        isbn = myBookDetailResponse.bookInfo.isbn,
                        content = memo
                    )
                    viewModel.postBookMemo(result, now().toString())
                    memo = ""
                }
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
fun DetailBookErrorView() {
    Text(text = "에러가 발생했습니다.")
}

@Composable
fun OneLineMemos(
    myBookDetailResponse : MyBookListResponse,
    memberPk: Long,
    isbn: String?
) {
    val viewModel: MyBookViewModel = hiltViewModel()
    val notesList by viewModel.notesList.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        isbn?.let {
            viewModel.getMyBookDetailResponse(memberPk,isbn)
        }
    }

    if(notesList != null) {
        notesList.forEach {note->
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)) {
                Text("${note.createdAt.take(10)} : ")
                Text(text = "${note.memo}")
            }
            Spacer(modifier = Modifier.padding(4.dp))
        }
    }
}