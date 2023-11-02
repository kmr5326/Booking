package com.booking.book.memberbook.controller;

import com.booking.book.global.jwt.JwtUtil;
import com.booking.book.memberbook.dto.response.MemberBookListResponse;
import com.booking.book.memberbook.service.MemberBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/book/member")
@RestController
public class MemberBookController {

    private final MemberBookService memberBookService;
    private static final String AUTHORIZATION = "Authorization";

    @GetMapping("/")
    public Flux<MemberBookListResponse> getMemberBookByMemberId(@RequestHeader(AUTHORIZATION) String token) {
        Long memberId = JwtUtil.getMemberIdByToken(token);
        log.info(" {} member request readBookList", memberId);
        return memberBookService.getMemberBookByMemberId(memberId);
    }
}
