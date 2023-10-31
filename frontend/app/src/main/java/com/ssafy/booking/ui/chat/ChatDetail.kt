package com.ssafy.booking.ui.chat

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ssafy.booking.R
import com.ssafy.booking.ui.common.TopBarChat
import com.ssafy.booking.viewmodel.ChatViewModel
import java.time.LocalDateTime

@Composable
fun ChatDetail(
    navController: NavController, chatViewModel: ChatViewModel
) {
    val chatId = navController.currentBackStackEntry?.arguments?.getString("chatId")

    val filteredMessages = messages.filter { it.chatId.toString() == chatId }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        if(chatId == "3") {
            TopBarChat(title = "자율프로젝트")
        } else {
            TopBarChat(title = "${chatId}번째 채팅방")
        }

        MessageList(
            filteredMessages, modifier = Modifier
                .weight(1f)
                .background(Color(0xFF9bbbd4))
        )

        InputText(
            chatViewModel,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
        )
    }
}

@Composable
fun MessageList(
    filteredMessages: List<MessageData>, modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState, modifier = modifier.padding(16.dp)
    ) {
        itemsIndexed(filteredMessages) { index, message ->
            val previousMessage = if (index > 0) filteredMessages[index - 1] else null
            val nextMessage = if (index < filteredMessages.size - 1) filteredMessages[index + 1] else null

            MessageItem(message, previousMessage, nextMessage)
        }
    }

    // 스크롤 최하단 이동
    LaunchedEffect(key1 = true) {  // true를 키로 주어 한 번만 실행되게 함
        listState.animateScrollToItem(index = filteredMessages.size - 1)
    }
}

@Composable
fun MessageItem(
    message: MessageData,
    previousMessage: MessageData?,
    nextMessage: MessageData?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row() {
            if (previousMessage?.member != message.member) {
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
                if (previousMessage?.member != message.member) {
                    Text(
                        text = "${message.member}",
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Row {
                    Text(
                        text = "${message.content}",
                        modifier = Modifier
                            .background(
                                Color.White, shape = RoundedCornerShape(10.dp)
                            )
                            .padding(8.dp)
                            .widthIn(max = 220.dp),
                        color = Color.Black,
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    // 시간을 표시하는 조건
                    if (nextMessage?.timestamp != message.timestamp || nextMessage?.member != message.member || nextMessage == null) {
                        Text(
                            text = String.format(
                                "%02d:%02d", message.timestamp.hour, message.timestamp.minute
                            ),
                            fontSize = 12.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.align(Alignment.Bottom)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputText(
    chatViewModel: ChatViewModel, modifier: Modifier = Modifier
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
                containerColor = Color.Transparent,   // 텍스트 필드의 배경색을 흰색으로 설정
                cursorColor = Color.Black,       // 커서의 색상을 검은색으로 설정
                focusedIndicatorColor = Color.Transparent,  // 포커스 시 아래지시선 색상을 투명하게 설정
                unfocusedIndicatorColor = Color.Transparent  // 비포커스 시 아래지시선 색상을 투명하게 설정
            )
        )

        IconButton(onClick = {
            Log.d("CHAT", "눌렀습니다.")
            chatViewModel.sendMessage(text)
        }, modifier = Modifier.padding(0.dp),  // IconButton의 패딩을 제거
            content = {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = null,
                    tint = Color.Black
                )
            })

    }
}


// ########################## 객체
data class MessageData(
    val messageId: Int,
    val chatId: Int,
    val member: String,
    val content: String,
    val timestamp: LocalDateTime
)

// ########################## 데이터
val currentTime = LocalDateTime.now()

val messages = listOf(
    MessageData(1, 1, "알림", "자율프로젝트에 참여했습니다!!", currentTime),
    MessageData(2, 3, "한결", "재만형 로그인 테스트 해봤어?", currentTime.plusMinutes(0)),
    MessageData(3, 3, "김재만", "안되서 고치는중인데", currentTime.plusMinutes(20)),
    MessageData(4, 3, "한결", "그거 내 문제인듯", currentTime.plusMinutes(21)),
    MessageData(5, 3, "한결", "로컬에선 됐는데", currentTime.plusMinutes(21)),
    MessageData(6, 3, "김재만", "프론트 쪽에서 서버쪽 SSL인증서 PEM키를 넣어서 okhttp 쪽에 넣으라해서", currentTime.plusMinutes(21)),
    MessageData(7, 3, "한결", "배포하니까 안됨", currentTime.plusMinutes(21)),
    MessageData(8, 3, "김재만", "넣었는데도 안돼..", currentTime.plusMinutes(21)),
    MessageData(9, 3, "김재만", "음 그렇구만", currentTime.plusMinutes(21)),
    MessageData(10, 3, "한결", "내가 고치고 말해줄게", currentTime.plusMinutes(21)),
    MessageData(11, 3, "한결", "일단 다른거 ㄱㄱ", currentTime.plusMinutes(21)),
    MessageData(12, 3, "김재만", "ㅋㅋㅋㅋㅋㅇㅋ", currentTime.plusMinutes(21)),
    MessageData(13, 3, "한결", "형 됐어", currentTime.plusMinutes(60)),
    MessageData(14, 3, "한결", "현영아 크롤링 했어?", currentTime.plusMinutes(70)),
    MessageData(15, 3, "서현영", "하고있어", currentTime.plusMinutes(83)),
    MessageData(16, 3, "한결", "굳굳", currentTime.plusMinutes(93)),

    )
