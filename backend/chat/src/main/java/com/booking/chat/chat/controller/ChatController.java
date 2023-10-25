package com.booking.chat.chat.controller;

import com.booking.chat.kafka.domain.KafkaMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {

    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    @PostMapping("/chat/{chatRoomId}")
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
