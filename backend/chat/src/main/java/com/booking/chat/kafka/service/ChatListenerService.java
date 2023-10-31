package com.booking.chat.kafka.service;

import com.booking.chat.kafka.domain.KafkaMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatListenerService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @KafkaListener(topics = "Chatroom-*", groupId = "chat", containerFactory = "kafkaListenerContainerFactory")
    public void listenGroupChat(KafkaMessage message, String chatroomId) {
        simpMessagingTemplate.convertAndSend("/subscribe/" + chatroomId, message); // WebSocket을 통해 클라이언트에게 메세지 전송
    }
}
