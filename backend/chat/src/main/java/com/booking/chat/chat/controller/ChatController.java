package com.booking.chat.chat.controller;

import com.booking.chat.chat.domain.Message;
import com.booking.chat.chat.service.MessageService;
import com.booking.chat.kafka.domain.KafkaMessage;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
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
    @MessageMapping("/message/{chatroomId}")
    public void sendMessage(@Payload KafkaMessage kafkaMessage, @DestinationVariable("chatroomId") Long chatroomId) {
        log.info(" {} user request send message to {} chatroom", kafkaMessage.getSenderId(), chatroomId);
        //MongoDB에 저장
        messageService.save(kafkaMessage, chatroomId);

        // Kafka로 메세지와 함께 채팅방 ID를 헤더에 추가하여 전달
        ProducerRecord<String, KafkaMessage> record = new ProducerRecord<>("Chatroom-" + chatroomId, null, null, kafkaMessage);
        record.headers().add("chatroomId", chatroomId.toString().getBytes(StandardCharsets.UTF_8));
        kafkaTemplate.send(record);
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
