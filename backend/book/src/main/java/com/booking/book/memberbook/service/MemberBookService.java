package com.booking.book.memberbook.service;

import com.booking.book.book.service.BookService;
import com.booking.book.memberbook.domain.MemberBook;
import com.booking.book.memberbook.dto.request.MemberBookRegistRequest;
import com.booking.book.memberbook.dto.response.MemberBookListResponse;
import com.booking.book.memberbook.dto.response.MemberBookResponse;
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
                                   .map(MemberBookResponse::new);
    }

    public Mono<MemberBook> registMemberBook(MemberBookRegistRequest memberBookRegistRequest) {
        MemberBook memberBook = MemberBook.from(memberBookRegistRequest);
        return memberBookRepository.save(memberBook);
    }
}
