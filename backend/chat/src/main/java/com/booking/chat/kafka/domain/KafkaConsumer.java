package com.booking.chat.kafka.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "testTopic", groupId = "testGroup")
    protected void consume(@Payload String payload, Acknowledgment acknowledgment) throws Exception {
        log.info("event : {} " , payload);

        acknowledgment.acknowledge();
    }
}
