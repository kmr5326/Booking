package com.booking.book.book.controller;

import com.booking.book.book.dto.response.BookResponse;
import com.booking.book.book.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/book")
@RestController
public class BookController {

    private final BookService bookService;

    @GetMapping("")
    public Flux<BookResponse> searchBookListByTitle(@RequestParam("title") String title) {
        log.info(" search request for the book {} ", title);
        return bookService.searchBookListByTitleAndRelevance(title);
    }

}
