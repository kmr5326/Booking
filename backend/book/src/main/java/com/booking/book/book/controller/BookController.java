package com.booking.book.book.controller;

import com.booking.book.book.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;
}
