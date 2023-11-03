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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.ssafy.domain.model.ChatCreateRequest
import com.ssafy.domain.model.ChatExitRequest
import com.ssafy.domain.model.ChatJoinRequest
import com.ssafy.domain.model.ChatRoom
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatHome(
    navController: NavController,
    appViewModel: AppViewModel,
) {
    val chatViewModel: ChatViewModel = hiltViewModel()
    Scaffold (
        topBar = {
            TopBar("채팅")
        },
        bottomBar = {
            BottomNav(navController, appViewModel)
        },
        modifier = Modifier.fillMaxSize()
    ) {paddingValues->
        Column (
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ){
            Box {
                Column {
                    Button(
                        onClick = {
                            navController.navigate("chatDetail/3")
                        }
                    ) {
                        Text("채팅방 이동")
                    }
                    Button(
                        onClick = {
                            val request = ChatCreateRequest(2, 7001, "독서모임")
                            chatViewModel.createChatRoom(request)
                        }
                    ) {
                        Text("채팅방 생성 API")
                    }
                    Button(
                        onClick = {
                            val request = ChatJoinRequest(2, 7001)
                            Log.d("CHAT", "${request}")
                            chatViewModel.joinChatRoom(request)
                        }
                    ) {
                        Text("채팅방 참가 API")
                    }
                    Button(
                        onClick = {
                            val request = ChatExitRequest(2, 7001)
                            Log.d("CHAT", "${request}")
                            chatViewModel.exitChatRoom(request)
                        }
                    ) {
                        Text("채팅방 나가기 API")
                    }
                    Button(
                        onClick = {
                        }
                    ) {
                        Text("소켓 연결 테스트")
                    }
                    ChatList()
                }
            }

        }
    }
}

@Composable
fun ChatList() {
    val chatViewModel: ChatViewModel = hiltViewModel()
    val chatListState = chatViewModel.chatListState.value

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
                painter = painterResource(id = R.drawable.chat3),
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