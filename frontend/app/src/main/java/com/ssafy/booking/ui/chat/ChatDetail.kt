package com.ssafy.booking.ui.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ssafy.booking.ui.common.TopBar
import com.ssafy.booking.viewmodel.AppViewModel
import com.ssafy.booking.viewmodel.ChatViewModel
import java.time.LocalDateTime

@Composable
fun ChatDetail(
    navController: NavController,
    appViewModel: AppViewModel,
    chatViewModel : ChatViewModel
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

        InputText(chatViewModel)
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
            .padding(vertical = 8.dp)
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
fun InputText(chatViewModel: ChatViewModel) {
    var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Enter Message") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {
                Log.d("CHAT","눌렀습니다.")
                chatViewModel.sendMessage(text)
            }
        ) {
            Text("전송")
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
    MessageData(2, 3,"김싸피", "1.", currentTime.plusMinutes(2)),
    MessageData(3, 3,"이싸피", "2", currentTime.plusMinutes(3)),
    MessageData(4, 3, "이싸피", "TEST", currentTime.plusMinutes(1)),
    MessageData(5, 3, "이싸피", "TEST2", currentTime.plusMinutes(6)),
    MessageData(6, 3, "이싸피", "TEST3", currentTime.plusMinutes(6)),
    MessageData(7, 3, "이싸피", "TEST4", currentTime.plusMinutes(6)),
    MessageData(8, 3, "이싸피", "TEST5", currentTime.plusMinutes(6)),
    MessageData(9, 3, "이싸피", "TEST6", currentTime.plusMinutes(6)),
    MessageData(10, 3, "이싸피", "TEST7", currentTime.plusMinutes(6)),
    MessageData(11, 3, "이싸피", "TEST7", currentTime.plusMinutes(6)),
    MessageData(12, 3, "이싸피", "TEST7", currentTime.plusMinutes(6)),
    MessageData(13, 3, "이싸피", "TEST7", currentTime.plusMinutes(6)),
    MessageData(14, 3, "이싸피", "TEST7", currentTime.plusMinutes(6)),
    MessageData(15, 3, "이싸피", "TEST7", currentTime.plusMinutes(6)),
    MessageData(16, 3, "이싸피", "TEST7", currentTime.plusMinutes(6)),

)
