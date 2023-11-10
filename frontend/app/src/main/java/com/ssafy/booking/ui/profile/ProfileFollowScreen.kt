package com.ssafy.booking.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ssafy.booking.ui.common.BackTopBar
import com.ssafy.booking.ui.common.TabBar
import com.ssafy.booking.viewmodel.MyPageViewModel
import com.ssafy.domain.model.mypage.UserFollowersResponse
import com.ssafy.domain.model.mypage.UserFollowingsResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileFollowScreen(
    memberPk: Long
) {
    val viewModel: MyPageViewModel = hiltViewModel()
    val combinedData by viewModel.combinedUserFollowData.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getUserFollowers(memberPk)
        viewModel.getUserFollowings(memberPk)
    }

    Scaffold(
        topBar = { BackTopBar("Follow 목록") },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            combinedData?.let { (followers, followings) ->
                if (followers != null && followings != null) {
                    IsSuccessView(followings, followers)
                } else {
                    IsLoadingView()
                }
            }
        }
    }
}

@Composable
fun IsSuccessView(
    userFollowingsResponse: UserFollowingsResponse,
    userFollowersResponse: UserFollowersResponse
) {
    TabBar(
        tabTitles = listOf("팔로워 ${userFollowersResponse.followersCnt}", "팔로잉 ${userFollowingsResponse.followingsCnt}"),
        contentForTab = { index ->
            // 인덱스 마다 @composable 함수 넣으면 됨.
            when (index) {
                0 -> FollowerListScreen(userFollowersResponse)
                1 -> FollowingListScreen(userFollowingsResponse)
            }
        }
    )
}

@Composable
fun IsLoadingView() {
    Text(text = "로딩중 ...")
}

@Composable
fun FollowerListScreen(
    userFollowersResponse: UserFollowersResponse
) {
    Column(
        modifier = Modifier.padding(24.dp)
    ) {
        if(userFollowersResponse.followersCnt == 0) {
            Text(text = "팔로워가 없어요..")
        } else {
            // lazyColumn 으로 팔로워 목록 구성하고
            // 해당 유저 클릭 시 해당 유저 프로필 페이지로 이동하기

        }
    }
}

@Composable
fun FollowingListScreen(
    userFollowingsResponse: UserFollowingsResponse
) {
    Column(
        modifier = Modifier.padding(24.dp)
    ) {
        if(userFollowingsResponse.followingsCnt == 0) {
            Text(text = "팔로잉이 없어요..")
        } else {
            // lazyColumn 으로 팔로잉 목록 구성하고
            // 해당 유저 클릭 시 해당 유저 프로필 페이지로 이동하기
        }
    }
}
