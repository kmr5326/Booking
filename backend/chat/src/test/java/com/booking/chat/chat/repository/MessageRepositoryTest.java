package com.booking.chat.chat.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.booking.chat.chat.domain.Message;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataMongoTest
class MessageRepositoryTest {

    @Autowired
    MessageRepository messageRepository;

    @BeforeEach
    void tearDown() throws Exception {
        messageRepository.deleteAll();
    }

    @Test
    @DisplayName("MongoDB 연동 테스트")
    void t1() throws Exception {

        Message testMessage = Message.builder()
                                     .message_id(1L)
                                     .member_id(1L)
                                     .chatroom_id(1L)
                                     .content("하이")
                                     .build();

        messageRepository.save(testMessage);

        List<Message> messageList = messageRepository.findAll();

        assertThat(messageList.size()).isEqualTo(1L);
    }

}