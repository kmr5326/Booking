package com.ssafy.booking.ui.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import com.ssafy.domain.model.mypage.UserFollowersResponse
import com.ssafy.domain.model.mypage.UserFollowingsResponse
import com.ssafy.domain.model.mypage.UserInfoResponse
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.LocalImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.util.DebugLogger
import com.ssafy.booking.di.NetworkModule_ProvideImageLoaderFactory.provideImageLoader
import com.ssafy.booking.di.NetworkModule_ProvideObjectStorageInterceptorFactory.provideObjectStorageInterceptor
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.ui.booking.tabTitles
import com.ssafy.booking.ui.common.BackTopBar
import com.ssafy.booking.ui.common.HorizontalDivider
import com.ssafy.booking.utils.ObjectStorageInterceptor
import com.ssafy.booking.viewmodel.MyBookViewModel
import com.ssafy.domain.model.mybook.MyBookListResponse
import com.ssafy.domain.model.mypage.UserInfoResponseByPk
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Composable
fun MyProfile(profileData: ProfileData) {
    val navController = LocalNavigation.current
    val viewModel: MyPageViewModel = hiltViewModel()
    val context = LocalContext.current
    val imageLoader = context.imageLoader
    val tokenDataSource = TokenDataSource(context)
    val memberPk : Long = tokenDataSource.getMemberPk()
    val isFollowNow = viewModel.isFollowNow.observeAsState()
    var followerCnt : Int by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.checkFollowNow(memberPk, profileData)
        profileData.followers?.followersCnt?.let {
            followerCnt = it
        }
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.background_color)
        ),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data("https://kr.object.ncloudstorage.com/booking-bucket/images/${profileData.myProfile?.memberPk}_profile.png")
                    .memoryCachePolicy(CachePolicy.DISABLED)
                    .addHeader("Host", "kr.object.ncloudstorage.com")
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                imageLoader=imageLoader,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                error = painterResource(id = R.drawable.basic_profile)
            )
            Spacer(modifier = Modifier.size(40.dp))
            Column {
                Text(text = "@${profileData.myProfile?.nickname}", color = colorResource(id = R.color.font_color))
                Spacer(modifier = Modifier.size(4.dp))
                Text(text = "읽은 책 : ${profileData.readBook!!.size}권", color = colorResource(id = R.color.font_color))
                Spacer(modifier = Modifier.size(4.dp))
                Row(
                    modifier = Modifier.clickable { navController.navigate("profile/follow/${profileData.myProfile!!.memberPk}") }
                ) {
                    Text(text = "팔로워 ${followerCnt}", color = colorResource(id = R.color.font_color))
//                    Text(text = "팔로워 ${profileData.followers?.followersCnt}", color = colorResource(id = R.color.font_color))
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(text = "팔로잉 ${profileData.followings?.followingsCnt}", color = colorResource(id = R.color.font_color))
                }
                Spacer(modifier = Modifier.size(4.dp))
                if(profileData.isI == true) {
                    Text(
                        text = "마일리지 : ${profileData.myProfile?.point}",
                        modifier = Modifier.clickable{
                            navController.navigate("pay/ready/0")
                        }
                        )
                } else {
                    isFollowNow?.let {
                        if (it.value == true) {
                            profileData.myProfile?.let {
                                Button(onClick = {
                                    viewModel.deleteFollow(it.memberPk)
                                    followerCnt--
                                }) {
                                    Text("언팔로우")
                                }
                            }
                        } else {
                            profileData.myProfile?.let {
                                Button(onClick = {
                                    viewModel.postFollow(it.memberPk)
                                    followerCnt++
                                }) {
                                    Text("팔로우")
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.size(20.dp))
            if(profileData.isI == true) {
                IconButton(
                    onClick = { navController.navigate("profile/modifier") },
                    modifier = Modifier.align(Alignment.Top)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = null,
                        tint = colorResource(id = R.color.font_color)
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileHome(
    navController: NavController,
    appViewModel: AppViewModel,
    yourMemberPk: Long
) {
    val context = LocalContext.current
    val tokenDataSource = TokenDataSource(context)
    val loginId: String? = tokenDataSource.getLoginId()
    val memberPk: Long = tokenDataSource.getMemberPk()
    val viewModel: MyPageViewModel = hiltViewModel()
    val myBookViewModel: MyBookViewModel = hiltViewModel()

    // 상태 관찰
    val profileState by viewModel.profileState.observeAsState()


    LaunchedEffect(Unit) {
        loginId?.let {
            Log.d("mypage", "$loginId")
            viewModel.getMyPage(memberPk, yourMemberPk)
        } ?: run {
            // 로그인 페이지로 이동시키는 버튼이 있는 화면 띄우기
        }
        memberPk?.let {
            myBookViewModel.getMyBookResponse(yourMemberPk)
        }
    }

    // 화면 표시
    when (profileState) {
        is UserProfileState.Loading -> LoadingView()
        is UserProfileState.Success -> ProfileView(data = (profileState as UserProfileState.Success).data, navController = navController, appViewModel = appViewModel, myBookViewModel = myBookViewModel)
        is UserProfileState.Error -> ErrorView(message = (profileState as UserProfileState.Error).message, navController = navController, appViewModel = appViewModel)
        else -> GuestView(navController = navController, appViewModel = appViewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(
    data: ProfileData,
    navController: NavController,
    appViewModel: AppViewModel,
    myBookViewModel : MyBookViewModel
) {
    val myBookState by myBookViewModel.myBookState.observeAsState()


//    fun provideObjectStorageInterceptor(): ObjectStorageInterceptor {
//        val accessKey = BuildConfig.naverAccess_key
//        val secretKey = BuildConfig.naverSecret_key
//        val region = "kr-standard"
//        return ObjectStorageInterceptor(accessKey, secretKey, region)
//    }
//
//    fun provideImageLoader(context: Context): ImageLoader {
//        val objectStorageInterceptor = provideObjectStorageInterceptor()
//
//        val okHttpClient = OkHttpClient.Builder()
//            .addInterceptor(objectStorageInterceptor)
//            .addInterceptor(HttpLoggingInterceptor().apply {
//                level = HttpLoggingInterceptor.Level.BODY
//            })
//            .build()
//
//        return ImageLoader.Builder(context)
//            .logger(DebugLogger())
//            .okHttpClient(okHttpClient)
//            .build()
//    }
//
//    val imageLoader = provideImageLoader(context)


    Scaffold(
        topBar = {
            if (data.isI == true) {
                TopBar("프로필")
            } else {
                BackTopBar(title = "프로필")
            }
        },
        bottomBar = {
            if (data.isI == true) {
                BottomNav(navController, appViewModel)
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            MyProfile(profileData = data)
            // 인자로 첫번째는 title 리스트, 두번째는 각 탭에 해당하는 @composable
            // 현재는 테스트용으로 하드코딩 해뒀음.
            TabBar(
                tabTitles = listOf("서재", "북킹"),
                contentForTab = { index ->
                    // 인덱스 마다 @composable 함수 넣으면 됨.
                    when (index) {
                        0 -> MyBook(myBookState = myBookState, data=data)
                        1 -> MyBookingList(data=data)
                    }
                }
            )
//            Spacer(modifier = Modifier.size(10.dp))
//            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
//            Spacer(modifier = Modifier.size(10.dp))
//            MyBook(myBookState = myBookState, data=data)
        }
    }
}

@Composable
fun LoadingView() {
    Box (
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    )  {
        Text("로딩중...")

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestView(
    navController: NavController,
    appViewModel: AppViewModel
) {
    Scaffold(
        topBar = {
            TopBar(title = "프로필")
        },
        bottomBar = {
            BottomNav(navController, appViewModel)
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
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
    message: String,
    navController: NavController,
    appViewModel: AppViewModel
) {
    Scaffold(
        topBar = {
            TopBar(title = "프로필")
        },
        bottomBar = {
            BottomNav(navController, appViewModel)
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
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
    val myProfile: UserInfoResponseByPk?,
    val readBook: List<MyBookListResponse>?,
    val followers: UserFollowersResponse?,
    val followings: UserFollowingsResponse?,
    val isI : Boolean,
)
