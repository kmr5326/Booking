package com.ssafy.booking.ui.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ssafy.booking.R
import com.ssafy.booking.di.App
import com.ssafy.booking.ui.common.TopBarChat
import com.ssafy.booking.viewmodel.ChatViewModel
import com.ssafy.booking.viewmodel.MyPageViewModel
import com.ssafy.booking.viewmodel.SocketViewModel
import com.ssafy.data.room.entity.MessageEntity
import com.ssafy.domain.model.ChatExitRequest
import com.ssafy.domain.model.KafkaMessage
import com.ssafy.domain.model.mypage.UserInfoResponseByPk
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetail(
    navController: NavController,
    socketViewModel: SocketViewModel
) {
    val chatId = navController.currentBackStackEntry?.arguments?.getString("chatId")
    val chatViewModel: ChatViewModel = hiltViewModel()
    val myPageViewModel: MyPageViewModel = hiltViewModel()
    var memberId by remember { mutableStateOf<Long?>(null) }
    var nickname by remember { mutableStateOf("") }

    val loginId = App.prefs.getLoginId()
    val getUserInfoResponse by myPageViewModel.getUserInfoResponse.observeAsState()

    // 자신의 정보 불러오기
    LaunchedEffect(loginId) {
        loginId?.let {
            myPageViewModel.getUserInfo(loginId)
        }
    }
    // 자신의 정보가 로드되면 memberId와 nickname을 설정
    getUserInfoResponse?.let { response ->
        memberId = response.body()?.memberPk
        nickname = response.body()?.nickname ?: ""
    }

    chatId?.let {
        LaunchedEffect(chatId) {
            socketViewModel.connectToChat(chatId)
            socketViewModel.postLastReadMessageId(chatId.toInt())
        }
    }

    val listState = rememberLazyListState()
    val topBarState = rememberTopAppBarState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)
    val coroutineScope = rememberCoroutineScope()

    val messages by socketViewModel.messages.observeAsState(initial = emptyList())
    if (chatId != null) {
        socketViewModel.loadLatestMessages(chatId)
    }

    // 나갈 때 소켓 연결 해제
    DisposableEffect(chatId) {
        onDispose {
            socketViewModel.disconnectChat()
        }
    }

//    if (chatId != null) {
//        socketViewModel.postLastReadMessageId(chatId.toInt())
//    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Drawer title", modifier = Modifier.padding(16.dp))
                Divider()
                Button(
                    onClick = {
                        val request = ChatExitRequest(chatId, memberId)
                        Log.d("CHAT", "$request")
                        chatViewModel.exitChatRoom(request)
                        navController.popBackStack()
                    }
                ) {
                    Text("채팅방 나가기")
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopBarChat(
                    title = "${chatId}번 채팅방",
                    onNavigationIconClick = {
                        coroutineScope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            contentWindowInsets = ScaffoldDefaults
                .contentWindowInsets
                .exclude(WindowInsets.navigationBars)
                .exclude(WindowInsets.ime),
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) { paddingValues ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                MessageList(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFF9bbbd4)),
                    listState,
                    messages,
                    memberId
                )
                chatId?.let {
                    InputText(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .imePadding(),
                        socketViewModel,
                        listState,
                        messages.size,
                        chatId,
                        memberId,
                        nickname
                    )
                }
            }
        }
    }
}

@Composable
fun MessageList(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    messages: List<MessageEntity>,
    memberId: Long?
) {
    val myPageViewModel: MyPageViewModel = hiltViewModel()
    // 메시지 아이템에 대한 유저 정보를 캐시하기 위한 맵
    val userInfoCache = remember { mutableMapOf<Long, UserInfoResponseByPk>() }
    LaunchedEffect(messages) {
        messages.forEach { message ->
            message.senderId?.toLong()?.let {senderId ->
                if(!userInfoCache.containsKey(senderId)) {
                    myPageViewModel.getUserInfoResponseByPk(senderId.toInt())
                }
            }
        }
    }
    LaunchedEffect(myPageViewModel.getUserInfoResponseByPk) {
        myPageViewModel.getUserInfoResponseByPk.observeForever {response ->
            response.body()?.let { userInfo ->
                userInfoCache[userInfo.memberPk] = userInfo
            }
        }
    }

    Box(
        modifier = modifier
            .background(Color(0xFF9bbbd4))
            .fillMaxSize()
    ) {
        LazyColumn(
            state = listState,
            modifier = modifier.padding(16.dp)
        ) {
            itemsIndexed(messages) { index, message ->
                val previousMessage = if (index > 0) messages[index - 1] else null
                val nextMessage =
                    if (index < messages.size - 1) messages[index + 1] else null

                MessageItem(message, previousMessage, nextMessage, memberId, userInfoCache)
            }
        }

        LaunchedEffect(messages) {
            if (messages.isNotEmpty()) {
                listState.animateScrollToItem(index = messages.size - 1)
            }
        }
    }
}

@Composable
fun MessageItem(
    message: MessageEntity,
    previousMessage: MessageEntity?,
    nextMessage: MessageEntity?,
    memberId: Long?,
    userInfoCache: MutableMap<Long, UserInfoResponseByPk>
) {
    val isOwnMessage = message.senderId?.toLong() == memberId
    val userInfo = userInfoCache[message.senderId?.toLong()]
//    Log.d("CHAT_DETAIL", "유저캐시맵 ${userInfoCache}")
//    Log.d("CHAT_DETAIL", "유저정보 $userInfo")

    val nextTime = nextMessage?.timeStamp?.let { formatTimestamp(it) }
    val curTime = formatTimestamp(message.timeStamp)
//    val prevTime = previousMessage?.timeStamp?.let { formatTimestamp(it) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = if (isOwnMessage) 6.dp else 0.dp),
            horizontalArrangement = if (isOwnMessage) Arrangement.End else Arrangement.Start

        ) {
            if (isOwnMessage) {
            } else if (previousMessage?.senderId != message.senderId) {
                AsyncImage(
                    model = userInfo?.profileImage,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    placeholder = ColorPainter(Color.LightGray),
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(20.dp))
                )
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.width(4.dp))

            Column {
                // 이름을 표시하는 조건
                if (!isOwnMessage && previousMessage?.senderId != message.senderId) {
                    Text(
                        text = "${userInfo?.nickname}"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Row(
                    horizontalArrangement = if (isOwnMessage) Arrangement.End else Arrangement.Start,
                    modifier = Modifier.fillMaxWidth() // 이 부분을 추가하여 전체 너비를 사용하도록 설정
                ) {
                    // 자신의 메시지인 경우, 시간을 먼저 표시
                    if (isOwnMessage &&
                        (nextMessage == null || nextTime != curTime || nextMessage.senderId != message.senderId)
                    ) {
                        Text(
                            text = curTime,
                            fontSize = 12.sp,
                            color = Color(0xFF556677),
                            modifier = Modifier.align(Alignment.Bottom)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    Text(
                        text = "${message.content}",
                        modifier = Modifier
                            .background(
                                color = if (isOwnMessage) Color(0xFFFEF01B) else Color.White,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(8.dp)
                            .widthIn(max = 220.dp),
                        color = Color.Black
                    )

                    // 다른 사람의 메시지인 경우, 메시지 뒤에 시간을 표시
                    if (!isOwnMessage &&
                        (nextMessage == null || nextTime != curTime || nextMessage.senderId != message.senderId)
                    ) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = curTime,
                            fontSize = 12.sp,
                            color = Color(0xFF556677),
                            modifier = Modifier.align(Alignment.Bottom)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputText(
    modifier: Modifier,
    socketViewModel: SocketViewModel,
    listState: LazyListState,
    messagesSize: Int,
    chatId: String?,
    memberId: Long?,
    nickname: String
) {
    var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        IconButton(
            onClick = {
                val message = KafkaMessage(
                    message = text.text,
                    senderId = memberId,
                    sendTime = LocalDateTime.now(),
                    senderName = nickname
                )
                socketViewModel.sendMessage(message, chatId?.toLong())
                text = TextFieldValue("")
            },
            enabled = text.text.isNotBlank(),
            modifier = Modifier.padding(0.dp),
            content = {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = null,
                    tint = if (text.text.isNotBlank()) Color.Black else Color.Gray
                )
            }

        )
    }
}

fun formatTimestamp(timestamp: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    val timestampWithoutMilliseconds = timestamp.substringBefore('.')
    val dateTime = LocalDateTime.parse(timestampWithoutMilliseconds, formatter)
    return "${dateTime.hour.toString().padStart(2, '0')}:${dateTime.minute.toString().padStart(2, '0')}"
}