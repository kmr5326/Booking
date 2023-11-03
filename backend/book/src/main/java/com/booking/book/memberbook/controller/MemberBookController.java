package com.booking.book.memberbook.controller;

import com.booking.book.global.jwt.JwtUtil;
import com.booking.book.memberbook.dto.request.MemberBookRegistRequest;
import com.booking.book.memberbook.dto.response.MemberBookListResponse;
import com.booking.book.memberbook.dto.response.MemberBookResponse;
import com.booking.book.memberbook.service.MemberBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @GetMapping("/{isbn}")
    public Mono<ResponseEntity<MemberBookResponse>> getMemberBookDetail(@RequestHeader(AUTHORIZATION) String token, @PathVariable String isbn) {
        Long memberId = JwtUtil.getMemberIdByToken(token);
        log.info(" {} member request detail member book : {} ", memberId, isbn);
        return memberBookService.getMemberBookDetail(memberId, isbn)
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Member has not read this book")))
                                .map(ResponseEntity::ok);
    }

    @PostMapping("/")
    public Mono<ResponseEntity<Void>> registMemberBook(@RequestHeader(AUTHORIZATION) String token, MemberBookRegistRequest memberBookRegistRequest) {
        return memberBookService.registMemberBook(memberBookRegistRequest)
            .flatMap(memberBook -> {
                log.info(" {} member regist book : {} ", memberBookRegistRequest.memberId(), memberBookRegistRequest.bookIsbn());
                return Mono.just(new ResponseEntity<Void>(HttpStatus.CREATED));
            })
            .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    //TODO : 메모 추가하기
}
