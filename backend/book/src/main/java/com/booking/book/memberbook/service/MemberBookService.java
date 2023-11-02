package com.booking.book.memberbook.service;

import com.booking.book.book.service.BookService;
import com.booking.book.global.exception.ErrorCode;
import com.booking.book.memberbook.dto.response.MemberBookListResponse;
import com.booking.book.memberbook.dto.response.MemberBookResponse;
import com.booking.book.memberbook.exception.MemberBookException;
import com.booking.book.memberbook.repository.MemberBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class MemberBookService {

    private final MemberBookRepository memberBookRepository;
    private final BookService bookService;

    public Flux<MemberBookListResponse> getMemberBookByMemberId(Long memberId) {
        return memberBookRepository.findAllByMemberIdOrderByCreatedAtDesc(memberId)
                                   .flatMap(memberBook ->
                                       bookService.findByIsbn(memberBook.getBookIsbn())
                                                  .map(MemberBookListResponse::new)
                                   );
    }

    public Mono<MemberBookResponse> getMemberBookDetail(Long memberId, String isbn) {
        return memberBookRepository.findByMemberIdAndBookIsbn(memberId, isbn)
                                   .switchIfEmpty(
                                       Mono.error(new MemberBookException(ErrorCode.MEMBER_NOT_READ)))
                                   .map(MemberBookResponse::new);
    }
}
