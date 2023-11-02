package com.booking.book.memberbook.repository;

import com.booking.book.memberbook.domain.MemberBook;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface MemberBookRepository extends ReactiveMongoRepository<MemberBook, Long> {

    Flux<MemberBook> findAllByMemberIdOrderByCreatedAtDesc(Long memberId);

}
