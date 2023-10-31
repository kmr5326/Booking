package com.booking.chat.socket.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.booking.chat.kafka.domain.KafkaMessage;
import com.booking.chat.kafka.domain.MessagePayload;
import com.booking.chat.socket.util.MessageFrameHandler;
import com.booking.chat.socket.util.StompTest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

public class WebSocketTest extends StompTest {

    @Test
    void t1() throws Exception {

        MessageFrameHandler<Long[]> handler = new MessageFrameHandler<>(Long[].class);
        this.stompSession.subscribe("/booking/chat", handler);

        KafkaMessage kafkaMessage = new KafkaMessage("메세지 전송", "전송자", LocalDateTime.now());
        String chatRoomId = "1";

        MessagePayload messagePayload = new MessagePayload(kafkaMessage, chatRoomId);

        this.stompSession.send("/subscribe/1", messagePayload);

        System.out.println("소켓 연결 여부 : " + this.stompSession.isConnected());
        List<Long> userList = List.of(handler.getCompletableFuture().get(3, TimeUnit.SECONDS));

        assertNotNull(userList);

    }


}
