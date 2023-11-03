package com.booking.book.book.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;
import static reactor.core.publisher.Mono.when;

import com.booking.book.book.dto.response.BookResponse;
import com.booking.book.util.ControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

class BookControllerTest extends ControllerTest {

    private final String BASE_URL = "/api/book";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.webTestClient = WebTestClient.bindToController(new BookController(bookService))
                                          .configureClient()
                                          .filter(
                                              documentationConfiguration(this.restDocumentation))
                                          .build();
    }

    @DisplayName("책 목록 검색 된다")
    @Test
    void searchBookListByTitleParameter() throws Exception {
        BookResponse bookResponse = new BookResponse("이기적 유전자", "작가", "이미지", "장르", "내용");
        Flux<BookResponse> mockResponse = Flux.just(bookResponse);

        when(bookService.searchBookListByTitleAndRelevance(any(String.class))).thenReturn(mockResponse);

        webTestClient.get()
            .uri(BASE_URL)
            .attribute("title", "이기적 유전자")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .consumeWith(document("book/list",
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("title").type(JsonFieldType.STRING).description("책 제목"),
                    fieldWithPath("author").type(JsonFieldType.STRING).description("작가"),
                    fieldWithPath("coverImage").type(JsonFieldType.STRING).description("책 커버 이미지"),
                    fieldWithPath("genre").type(JsonFieldType.STRING).description("책 장르"),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("책 소개")
                ))
            );
    }

}