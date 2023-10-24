package com.booking.chat.chat.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.booking.chat.chat.domain.Chat;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class ChatRepositoryTest {

    @Autowired
    ChatRepository chatRepository;

    @BeforeEach
    void tearDown() throws Exception {
        chatRepository.deleteAll();
    }

    @Test
    @DisplayName("MongoDB 연동 테스트")
    void t1() throws Exception {

        Chat testChat = Chat.builder()
                            .id(1L)
                            .author("sangwon")
                            .chatroom_id(1L)
                            .content("하이")
                            .build();

        chatRepository.save(testChat);

        List<Chat> chatList = chatRepository.findAll();

        assertThat(chatList.size()).isEqualTo(1L);
    }

}