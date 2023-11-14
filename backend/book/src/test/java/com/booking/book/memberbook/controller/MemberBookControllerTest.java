package com.booking.book.memberbook.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

import com.booking.book.book.dto.response.BookResponse;
import com.booking.book.memberbook.domain.MemberBook;
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
import java.util.ArrayList;

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

//    @DisplayName("내 서재 책 등록")
//    @Test
//    void t1() throws Exception {
//        MemberBookRegistRequest request=new MemberBookRegistRequest("닉네임","isbn");
////        MemberBook memberBook=new MemberBook("1","멤버 닉네임","책 isbn",new ArrayList<>())
//
//        when(memberBookService.registerMemberBook(any()).thenReturn(Mono.empty()));
//
//        webTestClient.get()
//                .uri(uriBuilder -> uriBuilder.path(BASE_URL + "/searchByTitle").queryParam("title", "이기적 유전자").build())
//                .attribute("title", "이기적 유전자")
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBody()
//                .consumeWith(document("book/listByTitle",
//                        preprocessResponse(prettyPrint()),
//                        responseFields(
//                                fieldWithPath("[].title").type(JsonFieldType.STRING).description("책 제목"),
//                                fieldWithPath("[].author").type(JsonFieldType.STRING).description("작가"),
//                                fieldWithPath("[].coverImage").type(JsonFieldType.STRING).description("책 커버 이미지"),
//                                fieldWithPath("[].genre").type(JsonFieldType.STRING).description("책 장르"),
//                                fieldWithPath("[].publishDate").description("책 출판일"),
//                                fieldWithPath("[].content").type(JsonFieldType.STRING).description("책 소개"),
//                                fieldWithPath("[].isbn").type(JsonFieldType.STRING).description("ISBN")
//                        ))
//                );
//    }

}