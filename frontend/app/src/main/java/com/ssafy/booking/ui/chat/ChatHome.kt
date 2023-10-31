package com.ssafy.booking.ui.chat

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ssafy.booking.R
import com.ssafy.booking.ui.common.BottomNav
import com.ssafy.booking.ui.common.TopBar
import com.ssafy.booking.viewmodel.AppViewModel
import com.ssafy.booking.viewmodel.DummyAppViewModel
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.viewmodel.ChatViewModel

@Composable
fun ChatHome(
    navController: NavController,
    appViewModel: AppViewModel,
    chatViewModel: ChatViewModel,
) {

    Column (){
        TopBar(title = "채팅방")

        Box {
            Column {
                ChatList(navController)
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(bottom = 100.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    onClick = {
//                        chatViewModel.connectSocket()
                        chatViewModel.handShakingService()
                    }
                ) {
                    Text("Test:소켓 연결")
                }
            }
            BottomNav(navController, appViewModel)
        }

    }


}

@Composable
fun ChatList(navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(chatItemList) { chat ->
            ChatItem(chat) {}
        }
    }
}

@Composable
fun ChatItem(
    chat: ChatData,
    onRowClick: (ChatData) -> Unit
) {
    val navController = LocalNavigation.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable(onClick = {
                onRowClick(chat)
                navController.navigate("chatDetail/${chat.chatId}")
            })
    ) {
        if(chat.chatHeadCount <= 1) {
            Image(
                painter = painterResource(id = chat.imageResId),
                contentDescription = "Chat Image",
                modifier = Modifier
                    .size(70.dp, 70.dp)
                    .clip(RoundedCornerShape(36.dp)) // 박스를 둥글게
            )
        } else {
            Image(
                painter = painterResource(id = chat.imageResId),
                contentDescription = "Chat Image",
                modifier = Modifier
                    .size(70.dp, 70.dp)
            )
        }


        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.width(200.dp),
        ) {
            Text(
                text = chat.chatTitle, maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = chat.chatContent,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
                fontSize=12.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally // 좌우 정렬
        ) {
            if(chat.chatHeadCount > 1) {
                Text(
                    text = "${chat.chatHeadCount} 명",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium,
                    fontSize=12.sp,
                )
            }

            // 나가기
            if(chat.chatType != "Notification") {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        Icons.Filled.ExitToApp , contentDescription = "chatExit"
                    )
                    Spacer(
                        modifier = Modifier.size(ButtonDefaults.IconSpacing)
                    )
                }
            }

        }
    }
}

// ########################## 객체
data class ChatData(
    val chatId : Int,
    val chatType : String,
    val imageResId: Int,
    val chatTitle: String,
    val chatContent: String,
    val chatHeadCount: Int
)
// ########################## 데이터
val chatItemList = listOf(
    ChatData(1,"Notification", R.drawable.chat1, "알림", "인간실격 독서토론에 참여했습니다!",1),
//    ChatData(2,"Person",R.drawable.chat2, "박희창", "@@@@@@@@@@@@@", 1),
    ChatData(3,"Booking",R.drawable.chat3, "인간실격 독서토론", "주말로 변경할 수 있나요?",3),
//    ChatData(4,"Booking",R.drawable.chat4, "남이야 잡화점", "안녕하세요!!",4)
)