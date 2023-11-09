package com.booking.book.memberbook.controller;

import com.booking.book.book.exception.BookException;
import com.booking.book.global.jwt.JwtUtil;
import com.booking.book.memberbook.dto.request.MemberBookRegistRequest;
import com.booking.book.memberbook.dto.request.RegisterNoteRequest;
import com.booking.book.memberbook.dto.response.MemberBookListResponse;
import com.booking.book.memberbook.dto.response.MemberBookResponse;
import com.booking.book.memberbook.service.MemberBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/{nickname}")
    public Flux<MemberBookResponse> getMemberBookByMemberId(@PathVariable String nickname) {
//        Long memberId = JwtUtil.getMemberIdByToken(token);
        log.info(" {} member request readBookList", nickname);
        return memberBookService.getMemberBookByMemberId(nickname);
    }

    @GetMapping("/{nickname}/{isbn}")
    public Mono<ResponseEntity<MemberBookResponse>> getMemberBookDetail(@PathVariable String nickname,@PathVariable String isbn) {
//        Long memberId = JwtUtil.getMemberIdByToken(token);
        log.info(" {} member request detail member book : {} ", nickname, isbn);
        return memberBookService.getMemberBookDetail(nickname, isbn)
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Member has not read this book")))
                                .map(ResponseEntity::ok);
    }

    @PostMapping("/")
    public Mono<ResponseEntity<Void>> registerMemberBook(@RequestHeader(AUTHORIZATION) String token,@RequestBody MemberBookRegistRequest memberBookRegistRequest) {
        log.info(" {} member register book : {} ", memberBookRegistRequest.nickname(), memberBookRegistRequest.bookIsbn());
        return memberBookService.registerMemberBook(memberBookRegistRequest)
            .flatMap(memberBook -> {
                return Mono.just(new ResponseEntity<Void>(HttpStatus.CREATED));
            })
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()));
    }

    //TODO : 메모 추가하기

    @PostMapping("/note")
    public Mono<ResponseEntity<String>> registerNote(@RequestHeader(AUTHORIZATION) String token,
                                                @RequestBody RegisterNoteRequest request) {
        log.info("한줄평 등록 요청 {}",request.toString());
        return memberBookService.registerNote(request)
                .flatMap(resp->Mono.just(ResponseEntity.ok().body(resp)))
                .onErrorResume(e->{
                    log.error(e.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body(e.getMessage()));
                });
    }
}
