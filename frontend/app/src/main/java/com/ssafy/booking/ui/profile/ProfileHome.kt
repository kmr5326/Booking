package com.ssafy.booking.ui.profile

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ssafy.booking.ui.common.BottomNav
import com.ssafy.booking.ui.common.TopBar
import com.ssafy.booking.viewmodel.AppViewModel
// TabBar Import
import com.ssafy.booking.ui.common.TabBar

@Composable
fun ProfileHome(
    navController: NavController, appViewModel: AppViewModel
) {
    Temp(navController, appViewModel)
}


@Composable
fun MyProfile(profileData : ProfileData) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.padding(16.dp)
            .fillMaxWidth()
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp),
        ){
            AsyncImage(
                model = profileData.imgUri,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                placeholder = ColorPainter(Color.DarkGray),
                modifier = Modifier.size(64.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.size(40.dp))
            Column {
                Text(text = profileData.name)
                Spacer(modifier = Modifier.size(4.dp))
                Text(text = "읽은 책 : ${profileData.readBookNumber}권")
                Spacer(modifier = Modifier.size(4.dp))
                Row {
                    Text(text = "팔로잉 ${profileData.followings}")
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(text = "팔로워 ${profileData.followers}")
                }
            }
            Spacer(modifier = Modifier.size(40.dp))
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = null,
                modifier = Modifier.align(Alignment.Top)
            )
        }
    }
}

// 메뉴 탭바 누가 만들지 ?? 공통으로 쓰이네
// 책 보이는 것도 공통으로 보임 (보이는 방식이 조금 다르지만)
// 팔로워, 팔로잉 사람 보이는 거랑 초대 목록 보이는 것도 동일한 양식

@Preview
@Composable
fun DefaultPreview() {
    val profileData = ProfileData(
        imgUri = "https://i.imgur.com/VbyOogn.jpg",
        name = "@uni.gy",
        readBookNumber = 10,
        followers = 390,
        followings = 255
    )
    MyProfile(profileData=profileData)
}

@Composable
fun Temp(
    navController: NavController, appViewModel: AppViewModel
) {
    val profileData = ProfileData(
        imgUri = "https://i.imgur.com/VbyOogn.jpg",
        name = "@uni.gy",
        readBookNumber = 10,
        followers = 390,
        followings = 255
    )

    Column {
        TopBar("프로필")
        MyProfile(profileData=profileData)
        // 인자로 첫번째는 title 리스트, 두번째는 각 탭에 해당하는 @composable
        // 현재는 테스트용으로 하드코딩 해뒀음.
        TabBar(
                tabTitles = listOf("내 서재", "북킹"),
            contentForTab = { index ->
                // 인덱스 마다 @composable 함수 넣으면 됨.
                when (index) {
                    0 -> Text("여기다가 내 서재 @Composable 넣으세요.")
                    1 -> Text("여기다가 북킹 @Composable 넣으세요.")
                }
            }
        )
    }


    BottomNav(navController, appViewModel)
}


// 우선 여기에 데이터 class 만들어 둠
data class ProfileData(
    val imgUri : String,
    val name : String,
    val readBookNumber : Number,
    val followers : Number,
    val followings : Number
)