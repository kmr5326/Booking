package com.booking.chat.chat.service;


import com.booking.chat.chat.domain.Message;
import com.booking.chat.chat.repository.MessageRepository;
import com.booking.chat.kafka.domain.KafkaMessage;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    public void processAndSend(KafkaMessage kafkaMessage, Long chatroomId) {
        save(kafkaMessage, chatroomId)
            .then(Mono.fromRunnable(() -> sendMessageToKafka(kafkaMessage, chatroomId)))
            .subscribe();
    }

    private void sendMessageToKafka(KafkaMessage kafkaMessage, Long chatroomId) {
        // Kafka로 메세지와 함께 채팅방 ID를 헤더에 추가하여 전달
        ProducerRecord<String, KafkaMessage> record = new ProducerRecord<>("Chatroom-" + chatroomId, null, null, kafkaMessage);
        record.headers().add("chatroomId", chatroomId.toString().getBytes(StandardCharsets.UTF_8));
        kafkaTemplate.send(record);
    }
    public Mono<Void> save(KafkaMessage message, Long chatroomId) {

        Message saveMessage = Message.builder()
                                     .chatroomId(chatroomId)
                                     .memberId(message.getSenderId()) // sender가 member_id라고 가정
                                     .content(message.getMessage())
                                     .build();


        return messageRepository.save(saveMessage).then();
    }

    public Flux<Message> findAllByRoomId(Long roomId) {

        return messageRepository.findByChatRoomId(roomId);
    }
}
