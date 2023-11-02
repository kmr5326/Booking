package com.booking.book.memberbook.controller;

import com.booking.book.memberbook.service.MemberBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/book/")
@RestController
public class MemberBookController {

    private final MemberBookService memberBookService;
}
