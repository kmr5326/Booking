package com.ssafy.booking.ui.book

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ssafy.booking.R
import com.ssafy.booking.model.BookSearchState
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.ui.common.BottomNav
import com.ssafy.booking.ui.common.TopBar
import com.ssafy.booking.viewmodel.AppViewModel
import com.ssafy.booking.viewmodel.BookSearchViewModel
import com.ssafy.domain.model.booksearch.BookSearchResponse
import retrofit2.Response


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookHome(
    navController: NavController,
    appViewModel: AppViewModel
) {
    val viewModel : BookSearchViewModel = hiltViewModel()
    
    val bookSearchState by viewModel.bookSearchState.observeAsState()

    // 입력값이 변경되었는지 추적하는 변수
    var isFirstChange by remember { mutableStateOf(true) }

    // 검색 창
    val bookTitle by viewModel.bookTitle.collectAsState("")

    LaunchedEffect(Unit) {
        viewModel.getBookLatest(1,16)
    }

    // 이전 페이지 구분
    val pageValue = true

    val handleClick = {
        if(pageValue) {

        } else {

        }
    }

    Scaffold (
        topBar = {
            if(pageValue) {
                TopBar(title = "도서 검색")
            } else {
                CenterAlignedTopAppBar(
                    title = { Text(text = "설정") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "뒤로가기"
                            )
                        }
                    },
                )
            }
        },
        bottomBar = {
            if(pageValue) {
                BottomNav(navController, appViewModel)
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = bookTitle, // 이 부분을 뷰모델의 상태로 연결하거나 필요에 따라 변경
                onValueChange = { newValue ->
                    viewModel.setBookTitle(newValue)
                    if(newValue == "") {
                        viewModel.bookSearchStateToInit()
                        isFirstChange = true
                    } else if (isFirstChange) {
                        viewModel.bookSearchStateToLoading()
                        isFirstChange = false
                    }
                },
                placeholder = {Text("찾는 도서가 있나요?",fontSize = 11.sp,color = Color.Gray)},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 4.dp)
                    .padding(bottom = 16.dp)
                    .height(50.dp),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF12BD7E),
                ),
                textStyle = TextStyle(color=Color.Gray,fontSize = 11.sp, baselineShift = BaselineShift.None),
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = Color(0xFF12BD7E) ) },
                trailingIcon = { IconButton(onClick = {
                    viewModel.setBookTitle("")
                    viewModel.bookSearchStateToInit()
                    isFirstChange = true
                }) {
                    Icon(Icons.Outlined.Clear, contentDescription = null, tint = Color.Gray)
                } },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (bookTitle != "") {
                            viewModel.bookSearch(bookTitle)
                        }
                    }
                ),
                shape = RoundedCornerShape(12.dp)
            )
            
            Spacer(modifier = Modifier.padding(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 4.dp)
                    .padding(horizontal = 16.dp)
            ) {
                when (bookSearchState) {
                    is BookSearchState.Loading -> BookLoadingView()
                    is BookSearchState.Success -> BookSuccessView(
                        data = (bookSearchState as BookSearchState.Success).data,
                        navController,
                        appViewModel,
                        viewModel
                    )
                    is BookSearchState.Error -> BookErrorView(message = (bookSearchState as BookSearchState.Error).message)
                    else -> BookInitView()
                }
            }
        }
    }
}


@Composable
fun BookLoadingView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookSuccessView(
    data: List<BookSearchResponse>,
    navController : NavController,
    appViewModel: AppViewModel,
    viewModel: BookSearchViewModel
) {
    if(data.isEmpty()) {
        Text("검색 결과가 없습니다.")
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 두 컬럼으로 설정
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            items(data.size) { index ->
                BookSearchItem(book = data[index])
            }
        }
    }
}

@Composable
fun BookSearchItem(book: BookSearchResponse) {
    val navController = LocalNavigation.current

    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable{
                navController.navigate("bookDetail/${book.isbn}")
            },
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.background_color)
        ),
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = book.coverImage,
                contentDescription = "책 커버 이미지",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                book.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun BookErrorView(message:String) {
    Text(text = "에러 : $message")
}


@Composable
fun BookInitView() {
    val viewModel : BookSearchViewModel = hiltViewModel()

    val getBookLatestResponse by viewModel.getBookLatestResponse.observeAsState()

    Text(text = "신간 도서")
    getBookLatestResponse?.let {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 15.dp)
        ) {
            items(it.body()!!) { book ->
                BookInitItem(book)
            }
        }
    }
}

@Composable
fun BookInitItem(book: BookSearchResponse) {
    val navController = LocalNavigation.current

    Column(
        modifier = Modifier
            .width(150.dp)
            .padding(12.dp)
            .clickable {
                navController.navigate("bookDetail/${book.isbn}")
            }
    ) {
        AsyncImage(
            model = book.coverImage,
            contentDescription = "책 커버 이미지",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = book.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

    }
}
