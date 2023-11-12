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
    private val _finalMessages = MutableLiveData<List<MessageEntity>>()
    val finalMessages: LiveData<List<MessageEntity>> = _finalMessages

    private val _totalMessageCount = MutableLiveData(20)
    val totalMessageCount: LiveData<Int> = _totalMessageCount

    // 채팅방 사용자 정보 가져오기
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


    // 각 채팅방 별 뷰모델 상태 초기화 (이전 채팅방이 나오는 문제 해결)
    // 메모리 누수 방지
    override fun onCleared() {
        super.onCleared()
        _finalMessages.value = emptyList()
        _userInfoMap.value = emptyMap()
    }

    // 전체 메시지 불러오기
    fun loadAllMessage(chatId: Int) {
        viewModelScope.launch {
            val count = _totalMessageCount.value ?: 20
            Log.d("TEST", "전체 메시지 불러오기 메시지 개수 : ${count}")
            messageDao.getAllMessage(chatId, count).asFlow()
                .collect { allMessages ->
                    _finalMessages.postValue(allMessages)
                }
        }
    }

    // 이전 메시지 더 불러오기 (무한 스크롤)
    fun loadMoreMessages(chatId: Int) {
        viewModelScope.launch {
            val currentMessages = _finalMessages.value.orEmpty()
            if (currentMessages.isNotEmpty()) {
                val lastMessageId = currentMessages.last().messageId
                lastMessageId?.let {
                    val newCount = (_totalMessageCount.value ?: 0) + 20
                    Log.d("TEST", "이전의 메시지 불러오기 메시지 개수 : $newCount")
                    messageDao.getMessagesBefore(chatId, lastMessageId, newCount).asFlow()
                        .distinctUntilChanged()
                        .collect { additionalMessages ->
                            _finalMessages.postValue(currentMessages + additionalMessages)
                            _totalMessageCount.postValue(newCount)
                        }
                }
            }
        }
    }


    // Room에 저장된 최신 메시지 불러오기 ForEach Mark -> SQLITE로 최적화
//    fun loadLatestMessage(chatId: Int) {
//        viewModelScope.launch {
//            messageDao.getUnusedMessage(chatId).asFlow()
//                .distinctUntilChanged()  // 중복 데이터 제거
//                .collect { newMessages ->
//                    val currentMessages = _finalMessages.value.orEmpty()
//                    val uniqueNewMessages =
//                        newMessages.filterNot { it.messageId in currentMessages.map { msg -> msg.messageId } } // 증복 등장 방지
//                    _finalMessages.postValue(uniqueNewMessages + currentMessages) // 새 메시지 추가
//                    val messageIds = uniqueNewMessages.mapNotNull { it.messageId }
//                    messageDao.markUsedMessages(messageIds)
//                    Log.d("TEST", "최신메시지")
//                    Log.d("TEST", "${uniqueNewMessages}")
//                }
//        }
//    }
//
//    // 2초 마다 최신 메시지 갱신
//    private val _setPollingMessage = MutableLiveData<Boolean>(false)
//    val setPollingMessage: LiveData<Boolean> get() = _setPollingMessage
//    private var PollingJob: Job? = null
//    fun starMessagePolling(chatId: Int) {
//        PollingJob = viewModelScope.launch(Dispatchers.Main) {
////            while (isActive) {
////                loadLatestMessage(chatId)
////                delay(2000)
////            }
//        }
//    }
//
//    // 갱신 중지
//    fun stopMessagePolling() {
//        PollingJob?.cancel()
//    }
//
//    // 갱신 ON/OFF
//    fun setPollingMessage(chatId: Int, toggle: Boolean) {
//        _setPollingMessage.value = toggle
//        if (toggle) {
//            starMessagePolling(chatId)
//        } else {
//            stopMessagePolling()
//        }
//    }

    // STOMP 메시지 수신할 때마다 마지막으로 읽은 메시지 갱신
    fun postLastReadMessageId(chatroomId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            val lastReadId = chatDao.getLastReadMessageId(chatroomId) ?: 1
            val lastMessageRequest = LastReadMessageRequest(lastMessageIndex = lastReadId)
            try {
                val messageResponses =
                    chatUseCase.postLastReadMessage(chatroomId, lastMessageRequest)
                val lastReadMessageIdx = messageResponses.maxOfOrNull { it.messageId } ?: 0
                val chatEntity = ChatEntity(chatroomId, lastReadMessageIdx)
                chatDao.updateLastReadMessage(chatEntity)
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
                    // 새 메시지 저장
                    if (existingEntity == null) {
                        messageDao.insertMessage(messageEntity)
                        val newCount = (_totalMessageCount.value ?: 0) + 1
                        Log.d("TEST", "새 메시지 저장 ${_totalMessageCount.value}")
                        _totalMessageCount.postValue(newCount)
                        // 이미 있는 메시지 읽은 수 업데이트
                    } else if (existingEntity.readCount!! < messageEntity.readCount!!) {
                        existingEntity.messageId?.let {
                            existingEntity.readCount?.let { cnt ->
                                messageDao.updateReadCount(it, cnt)
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

    // STOMP 소켓 연결
    fun connectToChat(chatId: String) {
        stompConnection = stomp.connect().subscribe {
            when (it.type) {
                Event.Type.OPENED -> {
                    // 토픽 구독
                    topic = stomp.join("/subscribe/$chatId").subscribe({ stompMessage ->
                        val kafkaMessage: KafkaMessage =
                            gson.fromJson(stompMessage, KafkaMessage::class.java)
                        viewModelScope.launch(Dispatchers.Main) {
                            try {
                                // 메시지 수신 응답 신호 보내기
                                postLastReadMessageId(chatId.toInt())
                            } catch (e: Exception) {
                                Log.e("CHAT", "SOCKETVM Error inserting message into database", e)
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

    // Kafka메시지 전송
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

    // 소켓 종료
    fun disconnectChat(chatId: String) {
        viewModelScope.launch {
            topic.dispose() // 구독 해지
            stompConnection.dispose() // STOMP 연결 해지
            // 연결 해제된 사람을 보내서 읽음 수 파악
            chatUseCase.deleteDisconnectSocket(chatId.toInt())
        }
    }
}
