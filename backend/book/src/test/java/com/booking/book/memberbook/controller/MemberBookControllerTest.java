package com.booking.book.memberbook.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

import com.booking.book.book.dto.response.BookResponse;
import com.booking.book.memberbook.domain.MemberBook;
import com.booking.book.memberbook.domain.Note;
import com.booking.book.memberbook.dto.request.MemberBookRegistRequest;
import com.booking.book.util.ControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class MemberBookControllerTest extends ControllerTest {

    private final String BASE_URL = "/api/book/member";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.webTestClient = WebTestClient.bindToController(new MemberBookController(memberBookService))
                .configureClient()
                .filter(
                        documentationConfiguration(this.restDocumentation))
                .build();
    }

    @DisplayName("내 서재 책 등록")
    @Test
    void t1() throws Exception {
//        MemberBookRegistRequest request=new MemberBookRegistRequest("닉네임","isbn");
        List<Note> notes=new ArrayList<>();
        notes.add(new Note("메모", LocalDateTime.now()));
        MemberBook memberBook=new MemberBook("1","멤버 닉네임","책 isbn",notes,LocalDateTime.now());

        when(memberBookService.registerMemberBook(any()).thenReturn(Mono.just(memberBook)));

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path(BASE_URL + "/").build())
                .header("Authorization","Bearer JWT")
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .consumeWith(document("memberBook/register",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("isbn").type(JsonFieldType.STRING).description("책 isbn")
                        ))
                );
    }

}