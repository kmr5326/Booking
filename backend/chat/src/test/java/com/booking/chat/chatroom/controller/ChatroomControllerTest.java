package com.booking.chat.chatroom.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

import com.booking.chat.chatroom.domain.Chatroom;
import com.booking.chat.chatroom.dto.request.InitChatroomRequest;
import com.booking.chat.chatroom.dto.request.JoinChatroomRequest;
import com.booking.chat.util.ControllerTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

class ChatroomControllerTest extends ControllerTest {

    private final String BASE_URL = "/api/chat/room";

    @BeforeEach
    void setUp() {
        this.webTestClient = WebTestClient.bindToController(new ChatroomController(chatroomService))
                                          .configureClient()
                                          .filter(
                                              documentationConfiguration(this.restDocumentation))
                                          .build();
    }

    @DisplayName("모임이 생성되면 채팅방 생성된다")
    @Test
    void initializeChatroomTest() throws Exception {

        InitChatroomRequest initChatroomRequest = new InitChatroomRequest(1L, 1L);
        Chatroom chatroom = new Chatroom(1L, 1L, List.of(1L), LocalDateTime.now());
        when(chatroomService.initializeChatroom(any())).thenReturn(Mono.just(chatroom));

        webTestClient.post()
                     .uri(BASE_URL + "/")
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(objectMapper.writeValueAsString(initChatroomRequest))
                     .exchange()
                     .expectStatus().isCreated()
                     .expectBody()
                     .consumeWith(document("chatroom/init",
                         preprocessRequest(prettyPrint()),
                         requestFields(
                             fieldWithPath("meetingId").type(JsonFieldType.NUMBER)
                                                       .description("모임 PK"),
                             fieldWithPath("leaderId").type(JsonFieldType.NUMBER)
                                                      .description("모임장 PK")
                         )
                     ));
    }

    @DisplayName("회원 채팅방 가입한다")
    @Test
    void joinChatroomTest() throws Exception {

        JoinChatroomRequest joinChatroomRequest = new JoinChatroomRequest(1L, 1L);
        when(chatroomService.joinChatroom(any())).thenReturn(Mono.empty());

        webTestClient.post()
                     .uri(BASE_URL + "/join")
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(objectMapper.writeValueAsString(joinChatroomRequest))
                     .exchange()
                     .expectStatus().isNoContent()
                     .expectBody()
                     .consumeWith(document("chatroom/join",
                         preprocessRequest(prettyPrint()),
                         requestFields(
                             fieldWithPath("meetingId").type(JsonFieldType.NUMBER)
                                                       .description("모임 PK"),
                             fieldWithPath("memberId").type(JsonFieldType.NUMBER)
                                                      .description("멤버 PK")
                         )
                     ));
    }
}