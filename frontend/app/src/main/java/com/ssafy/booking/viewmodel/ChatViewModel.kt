package com.ssafy.booking.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.ssafy.data.room.dao.ChatDao
import com.ssafy.data.room.entity.ChatEntity
import com.ssafy.domain.model.ChatCreateRequest
import com.ssafy.domain.model.ChatExitRequest
import com.ssafy.domain.model.ChatJoinRequest
import com.ssafy.domain.model.ChatRoom
import com.ssafy.domain.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val chatDao: ChatDao
) : ViewModel() {

    private val _chatListState = MutableLiveData<List<ChatRoom>>(listOf())
    val chatListState: LiveData<List<ChatRoom>> = _chatListState
    var errorMessage = mutableStateOf("")

    fun createChatRoom(request: ChatCreateRequest) {
        viewModelScope.launch {
            try {
                val response = chatRepository.postChatCreate(request)
                if (response.isSuccessful) {
                    Log.d("CHAT", "Chat room created successfully: $response")
                    loadChatList()
                } else {
                    Log.e("CHAT", "Error creating chat room: $response")
                }
            } catch (e: Exception) {
                Log.e("CHAT", "Exception creating chat room", e)
            }
        }
    }

    fun joinChatRoom(request: ChatJoinRequest) {
        viewModelScope.launch {
            try {
                val response = chatRepository.postChatJoin(request)
                if (response.isSuccessful) {
                    Log.d("CHAT", "Chat room joined successfully: $response")
                    loadChatList()
                } else {
                    Log.e("CHAT", "Error joining chat room: $response")
                }
            } catch (e: Exception) {
                Log.e("CHAT", "Exception joining chat room", e)
            }
        }
    }

    fun exitChatRoom(request: ChatExitRequest) {
        viewModelScope.launch {
            try {
                val response = chatRepository.postChatExit(request)
                if (response.isSuccessful) {
                    Log.d("CHAT", "Chat room exited successfully: $response")
                    loadChatList()
                } else {
                    Log.e("CHAT", "Error exiting chat room: $response")
                }
            } catch (e: Exception) {
                Log.e("CHAT", "Exception exiting chat room", e)
            }
        }
    }

    fun loadChatList() {
        viewModelScope.launch {
            try {
                val chatList = chatRepository.getChatList()
                _chatListState.value = chatList
                Log.d("CHAT", "Get Chat room List $chatListState")
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

    fun saveLocalChatId(chatroomId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val newChatEntity = ChatEntity(chatroomId = chatroomId, lastMessageIdx = 0)
            chatDao.insertChatIdFirstTime(newChatEntity)
        }
    }

    private val _lastReadMessageId = MutableStateFlow<Int?>(null)
    val lastReadMessageId: StateFlow<Int?> = _lastReadMessageId.asStateFlow()
    fun getLastReadMessageId(chatroomId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val lastReadId = chatDao.getLastReadMessageId(chatroomId) ?: 0
            _lastReadMessageId.value = lastReadId
        }
    }

    init {
        viewModelScope.launch {
            while (isActive) {
                loadChatList() // 채팅 목록을 로드하는 함수
                delay(2000L) // 5초간 대기
            }
        }
    }

}
