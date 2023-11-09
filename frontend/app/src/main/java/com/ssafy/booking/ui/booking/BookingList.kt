package com.ssafy.booking.ui.booking

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.ssafy.booking.R
import com.ssafy.booking.ui.common.BottomNav
import com.ssafy.booking.ui.profile.BookingList
import com.ssafy.booking.viewmodel.AppViewModel
import com.ssafy.booking.viewmodel.BookingViewModel
import com.ssafy.data.repository.token.TokenDataSource
import com.ssafy.domain.model.DeviceToken
import com.ssafy.domain.model.booking.BookingAll
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main(
    navController: NavController,
    appViewModel: AppViewModel
) {
    val bookingViewModel: BookingViewModel = hiltViewModel()
    val userInfoState by bookingViewModel.getUserInfoResponse.observeAsState()
    val context = LocalContext.current
    val tokenDataSource = TokenDataSource(context)
    val loginId = tokenDataSource.getLoginId()
    val deviceToken: String? = tokenDataSource.getDeviceToken()
    // LaunchedEffect를 사용하여 한 번만 API 호출
    LaunchedEffect(Unit) {
        bookingViewModel.postDeivceToken(DeviceToken(deviceToken))

        // 메인 화면 가자마자 userInfo 조회
        Log.d("test1", "${tokenDataSource.getLoginId()}")
        Log.d("test2", "$loginId")
        bookingViewModel.getUserInfo(loginId!!)

        // 전체 북킹 목록 조회
        bookingViewModel.getBookingAllList()
    }

    LaunchedEffect(userInfoState) {
        userInfoState?.body()?.let {
            tokenDataSource.putNickName(it.nickname)
            tokenDataSource.putProfileImage(it.profileImage)
            Log.d("test33", "$it")
            Log.d("test3", "${it.nickname}")
        }
    }
    Scaffold(
        topBar = {
            HomeTopBar(navController, appViewModel)
//            TopBar("하남동")
        },
        bottomBar = {
            BottomNav(navController, appViewModel)
        },
        floatingActionButton = {
            MyFloatingActionButton(navController, appViewModel)
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .fillMaxHeight()
        ) {
                BookList(navController, appViewModel,bookingViewModel)
            }
        }
    }
@Composable
fun BookList(navController: NavController, appViewModel: AppViewModel,bookingViewModel: BookingViewModel) {

    val bookingAllListState by bookingViewModel.getBookingAllList.observeAsState()
    // response가 not null 이면 바디 추출
    val bookingAllList = bookingAllListState?.body()
    LazyColumn(

        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .padding(horizontal = 8.dp, vertical = 15.dp)
    ) {
        bookingAllList?.let { bookings ->
            items(bookings) { booking ->
                BookItem(booking = booking,navController)
            }
        }
        }
    }
@Composable
fun BookItem(booking: BookingAll,navController: NavController) {
    val meetingId = booking.meetingId
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { // clickable 모디파이어 추가
                navController.navigate("bookingDetail/$meetingId") // 클릭 시 상세 화면으로 이동
            }
    ) {
        Image(
            painter = rememberImagePainter(
                data = booking.coverImage,
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
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = booking.meetingTitle, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = booking.bookTitle,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.LocationOn, contentDescription = "locate", modifier = Modifier.size(12.dp), tint = Color.Gray)
                Text(
//                    text = booking.lat.toString(),
                    text = "오선동",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                for (hashtag in booking.hashtagList) {
                    HashtagChip(tag = hashtag.content) // 각 해시태그에 대한 칩 생성
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Participants Icon",
                    modifier = Modifier.size(12.dp),
                    tint = Color.Gray
                )
                Text(
                    text = "${booking.curParticipants}명/${booking.maxParticipants}명",
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 2.dp),
                    color = Color.Gray
                )
            }
        }
    }
    Divider(
        color = Color.LightGray,
        thickness = 0.4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    )
}
// 해시태그 칩
@Composable
fun HashtagChip(tag: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(end = 4.dp) // 오른쪽 마진
            .background(Color.LightGray, RoundedCornerShape(10.dp)) // 둥근 사각형의 배경
            .padding(horizontal = 8.dp, vertical = 4.dp) // 내부 패딩
    ) {
        Text(
            text = "#${tag}",
            color = Color.White,
            fontSize = 10.sp, // 작은 글씨 크기
            maxLines = 1,
            overflow = TextOverflow.Ellipsis // 글이 넘치면 말줄임표로 처리
        )
    }
}

@Composable
fun MyFloatingActionButton(navController: NavController, appViewModel: AppViewModel) {
    FloatingActionButton(
        onClick = { navController.navigate("create/booking/isbn") },
        modifier = Modifier
            .padding(end = 16.dp, bottom = 10.dp)
            .size(65.dp),
        containerColor = Color(0xFF12BD7E),
        shape = CircleShape
        // 그냥 동그라미할지, + 모임생성할지 고민.

    ) {
        Icon(
            Icons.Filled.Add,
            contentDescription = "Localized description",
            modifier = Modifier.size(40.dp),
            tint = Color.White

        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(navController: NavController, appViewModel: AppViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF12BD7E),
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
            ) // 배경색과 모서리를 둥글게 설정
    ) {
        // 상단의 하남동과 설정 아이콘
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(top = 10.dp)
            ) {
                Text(text = "하남동", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFffffff))
                Icon(Icons.Filled.ArrowDropDown, contentDescription = null, tint = Color(0xFFffffff))
            }
            Icon(
                Icons.Rounded.Settings,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                tint = Color(0xFFffffff)
            )
        }
        // 검색 창
        var title by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(""))
        }
        OutlinedTextField(
            value = title, // 이 부분을 뷰모델의 상태로 연결하거나 필요에 따라 변경
            onValueChange = { title = it },
            placeholder = { Text("찾는 도서가 있나요?", fontSize = 11.sp, color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 4.dp)
                .padding(bottom = 16.dp)
                .height(50.dp)
                .background(Color.White, shape = RoundedCornerShape(3.dp)),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF12BD7E),
                unfocusedBorderColor = Color.White
            ),
            textStyle = TextStyle(color = Color.Gray, fontSize = 11.sp, baselineShift = BaselineShift.None),
            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = Color(0xFF12BD7E)) }
        )
    }
}
