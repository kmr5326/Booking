package com.booking.chat.test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.booking.chat.kafka.domain.KafkaMessage;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class SimpleKafkaTest {

    @Autowired
    private KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    @Autowired
    private KafkaTestUtils kafkaTestUtils;

    @Test
    @DisplayName("Kafka test 수행된다")
    public void testSendingAndReceivingMessage() {
        KafkaMessage messageToSend = KafkaMessage.builder()
                                                 .message("test message")
                                                 .sender("test sender")
                                                 .sendTime(LocalDateTime.now())
                                                 .build();

        final String TOPIC = "chatroom_1";
        // 메시지 보내기
        kafkaTemplate.send(TOPIC, messageToSend);

        // 메시지 받기
        KafkaMessage receivedMessage = (KafkaMessage) KafkaTestUtils.getSingleRecord(kafkaTestUtils.consumerFactory(), TOPIC).value();

        assertThat(receivedMessage).isEqualToComparingFieldByField(messageToSend);
    }

}
