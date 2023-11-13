package com.booking.chat.kafka.service;

import com.booking.chat.kafka.domain.KafkaMessage;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaService {

    private final KafkaAdmin kafkaAdmin;

    public void createTopic(Long chatroomId, int partitions, short replicationFactor) {
        String topicName = "Chatroom-%d".formatted(chatroomId);
        NewTopic newTopic = new NewTopic(topicName, partitions, replicationFactor);

        kafkaAdmin.createOrModifyTopics(newTopic);
        send(chatroomId);
    }

    public void createTopic(Long chatroomId) {
        createTopic(chatroomId, 1, (short) 1);
    }

    private void send(Long chatroomId) {
        KafkaMessage kafkaMessage = KafkaMessage.init();
        ProducerRecord<String, KafkaMessage> record = new ProducerRecord<>("Chatroom-%d".formatted(chatroomId), null, null, kafkaMessage);
        record.headers().add("chatroomId", chatroomId.toString().getBytes(StandardCharsets.UTF_8));

    }



}
