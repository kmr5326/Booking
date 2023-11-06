package com.ssafy.booking.ui.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ssafy.booking.R
import com.ssafy.booking.ui.common.TopBarChat
import com.ssafy.booking.viewmodel.MyPageViewModel
import com.ssafy.booking.viewmodel.SocketViewModel
import com.ssafy.data.repository.token.TokenDataSource
import com.ssafy.data.room.entity.MessageEntity
import com.ssafy.domain.model.KafkaMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetail(
    navController: NavController,
    socketViewModel: SocketViewModel
) {
    val chatId = navController.currentBackStackEntry?.arguments?.getString("chatId")

    var memberId by remember { mutableStateOf<Long?>(null) }
    var nickname by remember { mutableStateOf("") }
    val myPageViewModel: MyPageViewModel = hiltViewModel()
    val context = LocalContext.current
    val tokenDataSource = TokenDataSource(context)
    val loginId: String? = tokenDataSource.getLoginId()
    val getUserInfoResponse by myPageViewModel.getUserInfoResponse.observeAsState()

    LaunchedEffect(loginId) {
        val result = loginId?.let {
            myPageViewModel.getUserInfo(loginId)
        }
    }

    LaunchedEffect(getUserInfoResponse) {
        if (getUserInfoResponse != null) {
            Log.d("STOMP", "${getUserInfoResponse!!.body()}")
            memberId = getUserInfoResponse!!.body()?.memberPk
            nickname = getUserInfoResponse!!.body()?.nickname.toString()
        }

    }

    LaunchedEffect(memberId) {
        chatId?.let {
            socketViewModel.loadMessages(it)
            socketViewModel.connectToChat(it)
        }
    }

    val listState = rememberLazyListState()
    val messages by socketViewModel.messages.observeAsState(listOf())

    DisposableEffect(chatId) {
        onDispose {
            // 연결 종료
            chatId?.let {
                socketViewModel.disconnectChat()
            }
        }
    }


    Scaffold(
        topBar = {
            TopBarChat(title = "${chatId}번 채팅방")
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
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


@Composable
fun MessageList(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    messages: List<MessageEntity>,
    memberId: Long?
) {

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

                MessageItem(message, previousMessage, nextMessage, memberId)
            }
        }

        Log.d("MESSAGE", "${messages}")

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
    memberId: Long?
) {
    val isOwnMessage = message.senderId == memberId

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = if (isOwnMessage) 10.dp else 0.dp),
            horizontalArrangement = if (isOwnMessage) Arrangement.End else Arrangement.Start

        ) {
            if (isOwnMessage) {

            } else if (previousMessage?.senderId != message.senderId) {
                AsyncImage(
                    model = R.drawable.basic_profile,
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
                        text = "${message.senderName}",
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Row(
                    horizontalArrangement = if (isOwnMessage) Arrangement.End else Arrangement.Start,
                    modifier = Modifier.fillMaxWidth() // 이 부분을 추가하여 전체 너비를 사용하도록 설정
                ) {
                    // 자신의 메시지인 경우, 시간을 먼저 표시
                    if (isOwnMessage && (nextMessage?.sendTime != message.sendTime || nextMessage?.senderId != message.senderId || nextMessage == null)) {
                        Text(
                            text = message.sendTime?.let {
                                val updatedTime = it.plusHours(9) // 올바른 시간 계산을 위해 plusHours 사용
                                String.format("%02d:%02d", updatedTime.hour, updatedTime.minute)
                            } ?: "",
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
                        color = Color.Black,
                    )

                    // 다른 사람의 메시지인 경우, 메시지 뒤에 시간을 표시
                    if (!isOwnMessage && (nextMessage?.sendTime != message.sendTime || nextMessage?.senderId != message.senderId || nextMessage == null)) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = message.sendTime?.let {
                                val updatedTime = it.plusHours(9) // 올바른 시간 계산을 위해 plusHours 사용
                                String.format("%02d:%02d", updatedTime.hour, updatedTime.minute)
                            } ?: "",
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