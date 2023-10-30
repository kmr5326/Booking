package com.booking.chat.kafka.domain;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaProducer {

    private KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    public void send(String topic, String payload) {
        KafkaMessage kafkaMessage = new KafkaMessage(payload, "SangWon", LocalDateTime.now());
        kafkaTemplate.send(topic, kafkaMessage);
    }
}
