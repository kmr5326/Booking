package com.ssafy.booking.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.domain.model.ChatRoom
import com.ssafy.domain.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {
    var chatListState = mutableStateOf<List<ChatRoom>>(listOf())
    var errorMessage = mutableStateOf("")
    init {
        loadChatList()
    }
    private fun loadChatList() {
        viewModelScope.launch {
            try {
                chatListState.value = chatRepository.getChatList()
            } catch (e: HttpException) {
                // HTTP 에러 처리
                errorMessage.value = "네트워크 에러: ${e.code()} ${e.message}"
                Log.d("CHAT", "$errorMessage.value")
            } catch (e: IOException) {
                // 네트워크 연결 문제 등의 IOException 처리
                errorMessage.value = "네트워크 연결을 확인해 주세요."
                Log.d("CHAT", "$errorMessage.value")
            } catch (e: Exception) {
                // 그 외의 예외 처리
                errorMessage.value = "알 수 없는 에러 발생: ${e.message}"
                Log.d("CHAT", "$errorMessage.value")
            }
        }
    }
}