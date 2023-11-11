package com.ssafy.booking.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.StompClient
import com.google.gson.GsonBuilder
import com.ssafy.data.room.dao.ChatDao
import com.ssafy.data.room.dao.MessageDao
import com.ssafy.data.room.entity.ChatEntity
import com.ssafy.data.room.entity.MessageEntity
import com.ssafy.data.utils.LocalDateTimeDeserializer
import com.ssafy.data.utils.LocalDateTimeSerializer
import com.ssafy.domain.model.KafkaMessage
import com.ssafy.domain.model.LastReadMessageRequest
import com.ssafy.domain.model.mypage.UserInfoResponseByPk
import com.ssafy.domain.usecase.ChatUseCase
import com.ssafy.domain.usecase.MyPageUseCase
import com.ssafy.domain.usecase.OkhttpService
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.logging.Logger
import javax.inject.Inject

@HiltViewModel
class SocketViewModel @Inject constructor(
    private val messageDao: MessageDao,
    private val chatDao: ChatDao,
    private val chatUseCase: ChatUseCase,
    private val myPageUseCase: MyPageUseCase,
) : ViewModel() {
    lateinit var stompConnection: Disposable
    lateinit var topic: Disposable
    private val bookingwss = "wss://k9c206.p.ssafy.io:10001/booking/chat"
    private val intervalMillis = 1000L
    private val client = OkhttpService.OkHttpClientSingleton.provideOkHttpClient()
    val stomp = StompClient(client, intervalMillis).apply { this@apply.url = bookingwss }
    val gson =
        GsonBuilder().registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer())
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer()).create()

    private val _userInfoMap = MutableLiveData<Map<Long, UserInfoResponseByPk>>()
    val userInfoMap: LiveData<Map<Long, UserInfoResponseByPk>> = _userInfoMap
    fun loadUserInfo(memberId: Long) {
        viewModelScope.launch {
            val response = myPageUseCase.getUserInfoByPk(memberId)
            if (response.isSuccessful) {
                response.body()?.let { userInfoResponse ->
                    _userInfoMap.value =
                        _userInfoMap.value.orEmpty() + (memberId to userInfoResponse)
                }
            } else {
                Log.e("CHAT", "SOCKETVM ERROR")
            }
        }
    }

    private val _finalMessages = MutableLiveData<List<MessageEntity>>()
    val finalMessages: LiveData<List<MessageEntity>> get() = _finalMessages
    private var allMessagesSource: LiveData<List<MessageEntity>>? = null
    private var latestMessagesSource: LiveData<List<MessageEntity>>? = null

    // 전체 메시지 불러오기
    fun loadAllMessage(chatId: Int) {
        Log.d("CHAT", "loadAllMessage Start")
        viewModelScope.launch {
            messageDao.getAllMessage(chatId).asFlow()
                .collect { allMessages ->
                    _finalMessages.postValue(allMessages)
                }
        }
        Log.d("CHAT", "loadAllMessage End")
    }

    fun loadMoreMessages(chatId: Int) {
        Log.d("CHAT", "loadMoreMessages Start")

        viewModelScope.launch {
            val currentMessages = _finalMessages.value.orEmpty()
            if (currentMessages.isNotEmpty()) {
                val lastMessageId = currentMessages.last().messageId
                Log.d("CHAT", "라스트메시지아이디 ${lastMessageId}")
                if (lastMessageId != null) {
                    messageDao.getMessagesBefore(chatId, lastMessageId, 30).asFlow()
                        .collect { additionalMessages ->
                            `_finalMessages`.postValue(currentMessages + additionalMessages)
                        }
                }
            }
        }
        Log.d("CHAT", "loadMoreMessages End")

    }

    // 로컬에 있지만 불러오지 않은 메시지 불러오기
    fun loadLatestMessage(chatId: Int) {
        Log.d("CHAT", "loadLatestMessage Start")

        viewModelScope.launch {
            messageDao.getUnusedMessage(chatId).asFlow()
                .distinctUntilChanged()  // 중복 데이터 제거
                .collect { newMessages ->
                    val currentMessages = _finalMessages.value.orEmpty()
                    val uniqueNewMessages =
                        newMessages.filterNot { it.messageId in currentMessages.map { msg -> msg.messageId } } // 증복 등장 방지
                    _finalMessages.postValue(uniqueNewMessages + currentMessages) // 새 메시지 추가
                    uniqueNewMessages.forEach { newMessage ->
                        newMessage.messageId?.let { messageDao.markUsedMessage(it) } // 읽음 처리
                    }
                }
        }
        Log.d("CHAT", "loadLatestMessage End")

    }

    private val _setPollingMessage = MutableLiveData<Boolean>(false)
    val setPollingMessage: LiveData<Boolean> get() = _setPollingMessage
    private var PollingJob: Job? = null
    fun starMessagePolling(chatId: Int) {
        PollingJob = viewModelScope.launch(Dispatchers.Main) {
            delay(1000)
            while (isActive) {
                loadLatestMessage(chatId)
                Log.d("CHAT", "SOCKETVM 목록 갱신")
                delay(500)
            }
        }
    }

    fun stopMessagePolling() {
        Log.d("CHAT", "SOCKETVM 갱신 중지")
        PollingJob?.cancel()
    }

    fun setPollingMessage(chatId: Int, toggle: Boolean) {
        _setPollingMessage.value = toggle
        if (toggle) {
            starMessagePolling(chatId)
        } else {
            stopMessagePolling()
        }
    }

    // Observer 메모리 누수 방지
    override fun onCleared() {
//        Log.d("CHAT", "SOCKETVM onCleared")
        super.onCleared()
        allMessagesSource?.let { it.removeObserver {} }
        latestMessagesSource?.let { it.removeObserver {} }
        finalMessages.let { it.removeObserver {} }
        userInfoMap.let { it.removeObserver {} }
    }

    fun postLastReadMessageId(chatroomId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            val lastReadId = chatDao.getLastReadMessageId(chatroomId) ?: 1
            Log.d("CHAT", "SOCKETVM 마지막으로 읽은 메시지 보내기 ${lastReadId}")
            val lastMessageRequest = LastReadMessageRequest(lastMessageIndex = lastReadId)
            try {
                val messageResponses =
                    chatUseCase.postLastReadMessage(chatroomId, lastMessageRequest)
                val lastReadMessageIdx = messageResponses.maxOfOrNull { it.messageId } ?: 0
                val chatEntity = ChatEntity(chatroomId, lastReadMessageIdx)
                chatDao.updateLastReadMessage(chatEntity)
                Log.d("CHAT", "SOCKETVM 마지막으로 읽은 메시지 업데이트 ${chatEntity}")

                val messageEntities = messageResponses.map { response ->
                    MessageEntity(
                        chatroomId = response.chatroomId,
                        messageId = response.messageId,
                        senderId = response.senderId,
                        content = response.content,
                        readCount = response.readCount,
                        timeStamp = response.timestamp
                    )
                }
                // 메시지를 Room에 저장
                messageEntities.forEach { messageEntity ->
                    val existingEntity = messageDao.getMessageByChatIdAndMessageId(
                        messageEntity.chatroomId,
                        messageEntity.messageId
                    )
                    if (existingEntity == null) {
                        messageDao.insertMessage(messageEntity)
                        Log.d("CHAT", "SOCKETVM 신규 메시지 룸 저장 ${existingEntity}")
                    } else if (existingEntity.readCount != messageEntity.readCount) {
                        existingEntity.messageId?.let {
                            existingEntity.readCount?.let { cnt ->
                                messageDao.updateReadCount(it, cnt)
                                Log.d("CHAT", "SOCKETVM 읽음 처리 ${existingEntity}")
                            }
                        }
                    } else {
                        Log.d("CHAT", "SOCKETVM 중복된 메시지가 처리되었습니다.")
                    }
                }

            } catch (e: Exception) {
                Log.e("CHAT_DETAIL", "postLastRead $e")
            }
        }

    fun connectToChat(chatId: String) {
        stompConnection = stomp.connect().subscribe {
            when (it.type) {
                Event.Type.OPENED -> {
                    Log.d("CHAT", "SOCKETVM $it OPENED!!!")
                    topic = stomp.join("/subscribe/$chatId").subscribe({ stompMessage ->
                        // Get Message
                        Log.d("CHAT", "SOCKETVM Received: $stompMessage")
                        // Parse KafkaMessage
                        val kafkaMessage: KafkaMessage =
                            gson.fromJson(stompMessage, KafkaMessage::class.java)
                        Log.d("CHAT", "SOCKETVM Parsed Message: $kafkaMessage")
                        viewModelScope.launch(Dispatchers.Main) {
                            try {
                                Log.d("CHAT", "SOCKETVM 마지막으로 읽은 메시지 보내기 + 최신 메시지 받기")
                                postLastReadMessageId(chatId.toInt())
                                // delay(1000)
                                Log.d("CHAT", "SOCKETVM 최신 메시지 갱신")
                            } catch (e: Exception) {
                                Log.e(
                                    "CHAT",
                                    "SOCKETVM Error inserting message into database",
                                    e
                                )
                            }
                        }
                    }, { throwable ->
                        Log.e("CHAT", "SOCKETVM Error :", throwable)
                    })
                }

                Event.Type.CLOSED -> {
                    Log.d("CHAT", "SOCKETVM $it CLOSED!!!")
                }

                Event.Type.ERROR -> {
                    Log.d("CHAT", "SOCKETVM $it ERROR!!!")
                }

                else -> {
                    Log.d("CHAT", "SOCKETVM else")
                }
            }
        }
    }

    fun sendMessage(message: KafkaMessage, chatId: Long?) {
        val jsonMessage = gson.toJson(message)
        stomp.send("/publish/message/$chatId", jsonMessage).subscribe { success ->
            if (success) {
                Log.d("CHAT", "SOCKETVM chatting send is successful $jsonMessage")
            } else {
                Log.d("CHAT", "SOCKETVM failed to send message")
            }
        }
    }

    fun disconnectChat(chatId: String) {
        viewModelScope.launch {
            Log.d("CHAT", "SOCKETVM ${chatId} 나가기")
            topic.dispose() // 구독 해지
            stompConnection.dispose() // STOMP 연결 해지
            chatUseCase.deleteDisconnectSocket(chatId.toInt())
        }
    }

    fun onChatRoomChanged() {
        // 메시지 목록을 초기화하고 새 채팅방의 메시지를 로드
        _finalMessages.value = emptyList()
        _userInfoMap.value = emptyMap()
    }

}
