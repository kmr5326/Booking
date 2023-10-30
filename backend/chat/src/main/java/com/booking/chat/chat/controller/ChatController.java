package com.booking.chat.chat.controller;

import com.booking.chat.chat.service.MessageService;
import com.booking.chat.kafka.domain.KafkaMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController("/api/chat")
public class ChatController {

    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;
    private final MessageService messageService;

    // 클라이언트에서 /publish/message 로 메시지를 전송
    @MessageMapping("/message")
    public void sendMessage(@Payload KafkaMessage message, @Payload String chatroomId) {

        //MongoDB에 저장
        messageService.save(message, chatroomId);

        kafkaTemplate.send("Chatroom_" + chatroomId, message); // Kafka로 메세지 전달
    }

    @PostMapping("/{chatRoomId}")
    public ResponseEntity<String> sendMessage(@PathVariable String chatRoomId, @RequestBody String message) {

        KafkaMessage kafkaMessage = KafkaMessage.builder().message(message).build();

        try {
            kafkaTemplate.send("chat" + chatRoomId, kafkaMessage);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            log.error("error");
            return ResponseEntity.badRequest().body("fail");
        }
    }
}
