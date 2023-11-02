package com.ssafy.booking.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.domain.model.ChatCreateRequest
import com.ssafy.domain.model.ChatRoom
import com.ssafy.domain.repository.GetChatListRepository
import com.ssafy.domain.repository.PostChatCreateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatListRepository: GetChatListRepository,
    private val postChatCreateRepository: PostChatCreateRepository
) : ViewModel() {
    var chatListState = mutableStateOf<List<ChatRoom>>(listOf())
    var errorMessage = mutableStateOf("")

    fun createChatRoom(request: ChatCreateRequest) {
        viewModelScope.launch {
            try {
                val response = postChatCreateRepository.postChatCreate(request)
                if (response.isSuccessful && response.body() != null) {
                    Log.d("CHAT", "Chat room created successfully: ${response}")
                } else {
                    Log.e("CHAT", "Error creating chat room: ${response}")
                }
            } catch (e: Exception) {
                Log.e("CHAT", "Exception creating chat room", e)
            }
        }
    }

    fun loadChatList() {
        viewModelScope.launch {
            try {
                chatListState.value = getChatListRepository.getChatList()
            } catch (e: HttpException) {
                errorMessage.value = "네트워크 에러: ${e.code()} ${e.message}"
                Log.d("CHAT", "$errorMessage.value")
            } catch (e: IOException) {
                errorMessage.value = "네트워크 연결을 확인해 주세요."
                Log.d("CHAT", "$errorMessage.value")
            } catch (e: Exception) {
                errorMessage.value = "알 수 없는 에러 발생: ${e.message}"
                Log.d("CHAT", "$errorMessage.value")
            }
        }
    }

//    init {
//    loadChatList()
//    }
}