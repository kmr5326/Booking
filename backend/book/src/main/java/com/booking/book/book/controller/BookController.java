package com.booking.book.book.controller;

import com.booking.book.book.dto.response.BookResponse;
import com.booking.book.book.exception.BookException;
import com.booking.book.book.service.BookService;
import com.booking.book.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/book")
@RestController
public class BookController {

    private final BookService bookService;

    @GetMapping("/searchByTitle")
    public Flux<BookResponse> searchBookListByTitle(@RequestParam("title") String title) {
        log.info(" search request for the book {} ", title);
        return bookService.searchBookListByTitleAndRelevance(title);
    }

    @GetMapping("/searchByIsbn")
    public Mono<BookResponse> searchBookByIsbn(@RequestParam("isbn") String isbn) {
        log.info(" search by isbn request for the book {}", isbn);
        return bookService.findByIsbn(isbn)
                .map(BookResponse::new);
    }

    @GetMapping("/latest")
    public Flux<BookResponse> loadLatestBooks(@PageableDefault(size = 20, sort = "publishDate", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("신작 도서 요청 {}",pageable);
        return bookService.loadLatestBooks(pageable);
    }

}
