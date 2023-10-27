package com.ssafy.booking.ui.chat

import android.content.res.Resources.Theme
import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ssafy.booking.ui.common.TopBar
import com.ssafy.booking.viewmodel.AppViewModel
import java.time.LocalDateTime

@Composable
fun ChatDetail(
    navController: NavController,
    appViewModel: AppViewModel,
) {
    val chatId = navController
        .currentBackStackEntry
        ?.arguments
        ?.getString("chatId")

    val filteredMessages = messages.filter { it.chatId.toString() == chatId }

    Column {
        TopBar(title = "${chatId}번째 채팅방")
        MessageList(filteredMessages)

        Spacer(modifier = Modifier.weight(1f))

        InputText()
    }
}

@Composable
fun MessageList(
    filteredMessages : List<MessageData>
) {
    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        items(filteredMessages) { message ->
            MessageItem(message)
        }
    }
}

@Composable
fun MessageItem(message: MessageData) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row() {
            Text(
                text = "${message.member}",
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${message.content}",
                modifier = Modifier
                    .background(Color(0xFF00C68E), shape = RoundedCornerShape(10.dp))
                    .padding(8.dp)
                    .width(220.dp),
                color = Color.White,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = String.format("%02d:%02d", message.timestamp.hour, message.timestamp.minute),
                modifier = Modifier.align(Alignment.Bottom)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputText() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Enter Message") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {
            }
        ) {
            Text("Send")
        }
    }
}


// ########################## 객체
data class MessageData(
    val messageId: Int,
    val chatId : Int,
    val member: String,
    val content: String,
    val timestamp: LocalDateTime
)

// ########################## 데이터
val currentTime = LocalDateTime.now()

val messages = listOf(
    MessageData(1, 1, "알림", "인간실격 독서토론에 참여했습니다!!", currentTime),
    MessageData(2, 2,"박희창", "저 가입신청 좀 받아주세요 12312@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\$%\$\$\$\$\$\$\$\$\$\$\$\$\$\$\$\$\$\$\$@@@@@@@@@@@@@@", currentTime.plusMinutes(1)),
    MessageData(3, 3,"김싸피", "오늘 날씨가 좋네요.", currentTime.plusMinutes(2)),
    MessageData(4, 3,"이싸피", "네, 정말로요!", currentTime.plusMinutes(3)),
    MessageData(5, 3, "이싸피", "주말로 변경할 수 있나요?", currentTime.plusMinutes(1)),
    MessageData(6, 4, "박싸피", "ㅎㅇ", currentTime.plusMinutes(3)),
    MessageData(7, 4, "이싸피", "ㅂㅇ", currentTime.plusMinutes(4)),
    MessageData(8, 4, "박싸피", "ㅂㅇ", currentTime.plusMinutes(5)),
    MessageData(9, 4, "이싸피", "안녕하세요!!", currentTime.plusMinutes(6)),
)
