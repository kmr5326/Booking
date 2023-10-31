package com.booking.chat.chat.controller;

import com.booking.chat.chat.domain.Message;
import com.booking.chat.chat.service.MessageService;
import com.booking.chat.kafka.domain.KafkaMessage;
import com.booking.chat.kafka.domain.MessagePayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@RestController("/api/chat")
public class ChatController {

    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;
    private final MessageService messageService;

    // 클라이언트에서 /publish/message 로 메시지를 전송
    @MessageMapping("/message")
    public void sendMessage(@Payload MessagePayload messagePayload) {

        KafkaMessage message = messagePayload.getKafkaMessage();
        String chatroomId = messagePayload.getChatroomId();

        //MongoDB에 저장
        messageService.save(message, chatroomId);

        kafkaTemplate.send("Chatroom-" + chatroomId, message); // Kafka로 메세지 전달
    }

    @GetMapping(value = "/{chatroomId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Message> findAllByRoomId(@PathVariable Long chatroomId) {

        return messageService.findAllByRoomId(chatroomId);
    }

//    @PostMapping("/{chatRoomId}")
//    public ResponseEntity<String> sendMessage(@PathVariable String chatRoomId, @RequestBody String message) {
//
//        KafkaMessage kafkaMessage = KafkaMessage.builder().message(message).build();
//
//        try {
//            kafkaTemplate.send("chat" + chatRoomId, kafkaMessage);
//            return ResponseEntity.ok("success");
//        } catch (Exception e) {
//            log.error("error");
//            return ResponseEntity.badRequest().body("fail");
//        }
//    }
}
