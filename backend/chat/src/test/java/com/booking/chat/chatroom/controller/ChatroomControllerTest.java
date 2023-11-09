package com.booking.chat.chatroom.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

import com.booking.chat.chatroom.domain.Chatroom;
import com.booking.chat.chatroom.dto.request.ExitChatroomRequest;
import com.booking.chat.chatroom.dto.request.InitChatroomRequest;
import com.booking.chat.chatroom.dto.request.JoinChatroomRequest;
import com.booking.chat.chatroom.dto.response.ChatroomListResponse;
import com.booking.chat.util.ControllerTest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
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

        InitChatroomRequest initChatroomRequest = new InitChatroomRequest(1L, 1L, "미팅 이름");
        Chatroom chatroom = Chatroom.createWithLeader(initChatroomRequest);
        when(chatroomService.initializeChatroom(any())).thenReturn(Mono.just(chatroom));

        webTestClient.post()
                     .uri(BASE_URL + "/")
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(objectMapper.writeValueAsString(initChatroomRequest))
                     .exchange()
                     .expectStatus()
                     .isCreated()
                     .expectBody()
                     .consumeWith(document("chatroom/init",
                         preprocessRequest(prettyPrint()),
                         requestFields(
                             fieldWithPath("meetingId").type(JsonFieldType.NUMBER)
                                                       .description("모임 PK"),
                             fieldWithPath("leaderId").type(JsonFieldType.NUMBER)
                                                      .description("모임장 PK"),
                             fieldWithPath("meetingTitle").type(JsonFieldType.STRING)
                                                          .description("모임 제목")
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
                     .expectStatus()
                     .isNoContent()
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

    @DisplayName("회원 채팅방 탈퇴한다")
    @Test
    void exitChatroomTest() throws Exception {

        ExitChatroomRequest exitChatroomRequest = new ExitChatroomRequest(1L, 1L);
        when(chatroomService.exitChatroom(any())).thenReturn(Mono.empty());

        webTestClient.post()
                     .uri(BASE_URL + "/exit")
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(objectMapper.writeValueAsString(exitChatroomRequest))
                     .exchange()
                     .expectStatus()
                     .isNoContent()
                     .expectBody()
                     .consumeWith(document("chatroom/exit",
                         preprocessRequest(prettyPrint()),
                         requestFields(
                             fieldWithPath("meetingId").type(JsonFieldType.NUMBER)
                                                       .description("모임 PK"),
                             fieldWithPath("memberId").type(JsonFieldType.NUMBER)
                                                      .description("멤버 PK")
                         )
                     ));
    }

    @DisplayName("회원 채팅방 조회된다")
    @Test
    void getChatroomListTest() throws Exception {

        ChatroomListResponse chatroomListResponse = new ChatroomListResponse(1L, 1L, "meetingTitle","lastMessage",
            List.of(1L, 2L, 3L));

        ChatroomListResponse chatroomListResponse2 = new ChatroomListResponse(2L, 1L, "meetingTitle2","lastMessage2",
            List.of(1L, 2L, 3L));

        when(chatroomService.getChatroomListByMemberIdOrderByDesc(any())).thenReturn(
            Flux.just(chatroomListResponse, chatroomListResponse2));

        webTestClient.get()
                     .uri(BASE_URL + "/list")
                     .header("Authorization", "Bearer: token")
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody()
                     .consumeWith(document("chatroom/list",
                         preprocessResponse(prettyPrint()),
                         responseFields(
                             fieldWithPath("[]").description("속한 채팅방 목록 dto"),
                             fieldWithPath("[].chatroomId").type(JsonFieldType.NUMBER)
                                                           .description("모임 PK"),
                             fieldWithPath("[].meetingTitle").type(JsonFieldType.STRING)
                                                             .description("미팅방 이름"),
                             fieldWithPath("[].memberList[]").type(JsonFieldType.ARRAY)
                                                             .description("채팅방 멤버들 PK 배열")
                         )
                     ));
    }

    @DisplayName("회원 채팅방 입장하면, 마지막 읽은 메세지부터 전송한다")
    @Test
    void enterChatroomTest() throws Exception {

    }
}