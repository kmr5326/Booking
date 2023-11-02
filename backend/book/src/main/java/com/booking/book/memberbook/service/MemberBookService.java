package com.booking.book.memberbook.service;

import com.booking.book.memberbook.dto.response.MemberBookListResponse;
import com.booking.book.memberbook.repository.MemberBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Service
public class MemberBookService {

    private final MemberBookRepository memberBookRepository;

    public Flux<MemberBookListResponse> getMemberBookByMemberId(Long memberId) {
        return memberBookRepository.findAllByMemberIdOrderByCreatedAtDesc(memberId)
                                   .map(MemberBookListResponse::new);
    }
}
