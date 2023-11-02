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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ssafy.booking.R
import com.ssafy.booking.ui.common.BottomNav
import com.ssafy.booking.ui.common.TopBar
import com.ssafy.booking.viewmodel.AppViewModel
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.viewmodel.ChatViewModel
import com.ssafy.booking.viewmodel.SocketViewModel
import com.ssafy.domain.model.ChatRoom

@Composable
fun ChatHome(
    navController: NavController,
    appViewModel: AppViewModel,
    socketViewModel: SocketViewModel,
    chatViewModel: ChatViewModel
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
                    .padding(bottom = 200.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    onClick = {
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
fun ChatList(
    navController: NavController,
) {
    val chatViewModel: ChatViewModel = hiltViewModel()
    val chatListState = chatViewModel.chatListState.value

    Log.d("CHAT", "$chatListState")
    val chatList = chatListState

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(chatList) { chat ->
            ChatItem(chat) {

            }
        }
    }
}

@Composable
fun ChatItem(
    chat: ChatRoom,
    onRowClick: (ChatRoom) -> Unit
) {
    val navController = LocalNavigation.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable(onClick = {
                onRowClick(chat)
                navController.navigate("chatDetail/${chat.chatroomId}")
            })
    ) {
        if(chat.memberList.size <= 1) {
            Image(
                painter = painterResource(id = R.drawable.chat1),
                contentDescription = "Chat Image",
                modifier = Modifier
                    .size(70.dp, 70.dp)
                    .clip(RoundedCornerShape(36.dp)) // 박스를 둥글게
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.chat3),
                contentDescription = "Chat Image",
                modifier = Modifier
                    .size(70.dp, 70.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.width(200.dp),
        ) {
            Row (verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = chat.meetingTitle, maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                if(chat.memberList.size > 1) {
                    Text(
                        text = "${chat.memberList.size}",
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = chat.meetingTitle,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
                fontSize=12.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}
