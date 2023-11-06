package com.ssafy.booking.ui.profile

import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ssafy.booking.R
import com.ssafy.booking.model.UserProfileState
import com.ssafy.booking.ui.common.BottomNav
import com.ssafy.booking.ui.common.TopBar
import com.ssafy.booking.viewmodel.AppViewModel
import com.ssafy.data.repository.token.TokenDataSource
// TabBar Import
import com.ssafy.booking.ui.common.TabBar
import com.ssafy.booking.viewmodel.MyPageViewModel
import com.ssafy.domain.model.mypage.FollowersList
import com.ssafy.domain.model.mypage.UserFollowersResponse
import com.ssafy.domain.model.mypage.UserFollowingsResponse
import com.ssafy.domain.model.mypage.UserInfoResponse
import retrofit2.Response
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData


@Composable
fun MyProfile(profileData : ProfileData) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.background_color)
        ),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp),
        ){
            AsyncImage(
                model = profileData.myProfile?.profileImage,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                placeholder = ColorPainter(Color.DarkGray),
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.size(40.dp))
            Column {
                Text(text = "@${profileData.myProfile?.nickname}", color=colorResource(id = R.color.font_color))
                Spacer(modifier = Modifier.size(4.dp))
                Text(text = "읽은 책 : ${profileData.readBookNumber}권", color=colorResource(id = R.color.font_color))
                Spacer(modifier = Modifier.size(4.dp))
                Row {
                    Text(text = "팔로잉 ${profileData.followings?.followingsCnt}", color=colorResource(id = R.color.font_color))
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(text = "팔로워 ${profileData.followers?.followersCnt}", color=colorResource(id = R.color.font_color))
                }
            }
            Spacer(modifier = Modifier.size(40.dp))
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = null,
                modifier = Modifier.align(Alignment.Top),
                tint = colorResource(id = R.color.font_color)
            )
        }
    }
}


@Composable
fun MyBook() {
    Column(
        modifier = Modifier
            .padding(32.dp)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Text("내 서재")
    }
}

// 북킹 Composable
@Composable
fun BookingList() {
    Column(
        modifier = Modifier
            .padding(32.dp)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Text("북킹")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileHome(
    navController: NavController, appViewModel: AppViewModel
) {
    val context = LocalContext.current
    val tokenDataSource = TokenDataSource(context)

    val loginId : String? = tokenDataSource.getLoginId()

    val viewModel : MyPageViewModel = hiltViewModel()

    // 상태 관찰
    val profileState by viewModel.profileState.observeAsState()

    LaunchedEffect(Unit) {
        loginId?.let {
            Log.d("mypage","$loginId")
            viewModel.getMyPage(loginId)
        } ?: run {
            // 로그인 페이지로 이동시키는 버튼이 있는 화면 띄우기

        }
    }

    // 화면 표시
    when (profileState) {
        is UserProfileState.Loading -> LoadingView()
        is UserProfileState.Success -> ProfileView(data = (profileState as UserProfileState.Success).data, navController = navController, appViewModel = appViewModel)
        is UserProfileState.Error -> ErrorView(message = (profileState as UserProfileState.Error).message, navController = navController, appViewModel = appViewModel)
        else -> GuestView(navController = navController, appViewModel = appViewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(
    data: ProfileData, navController: NavController, appViewModel: AppViewModel
) {
    Scaffold (
        topBar = {
            TopBar("프로필")
        },
        bottomBar = {
            BottomNav(navController, appViewModel)
        },
        modifier = Modifier.fillMaxSize()
    ) {paddingValues->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            MyProfile(profileData=data)
            // 인자로 첫번째는 title 리스트, 두번째는 각 탭에 해당하는 @composable
            // 현재는 테스트용으로 하드코딩 해뒀음.
            TabBar(
                tabTitles = listOf("내 서재", "북킹"),
                contentForTab = { index ->
                    // 인덱스 마다 @composable 함수 넣으면 됨.
                    when (index) {
                        0 -> MyBook()
                        1 -> BookingList()
                    }
                }
            )
        }
    }
}

@Composable
fun LoadingView() {
    Text("로딩중...")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestView(
    navController : NavController,
    appViewModel : AppViewModel
) {
    Scaffold(
        topBar = {
            TopBar(title = "프로필")
        },
        bottomBar = {
            BottomNav(navController, appViewModel)
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("로그인을 하고 와주세요...")
            // 로그인 페이지로 넘기는 버튼 만들기
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorView(
    message : String,
    navController : NavController,
    appViewModel : AppViewModel
) {
    Scaffold(
        topBar = {
            TopBar(title = "프로필")
        },
        bottomBar = {
            BottomNav(navController, appViewModel)
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("에러페이지 에러 : $message")
            // 로그인 페이지로 넘기는 버튼 만들기
        }
    }

}

// sealed class 만들어 둠
data class ProfileData(
    val myProfile : UserInfoResponse?,
    val readBookNumber : Number = 0,
    val followers : UserFollowersResponse?,
    val followings : UserFollowingsResponse?
)