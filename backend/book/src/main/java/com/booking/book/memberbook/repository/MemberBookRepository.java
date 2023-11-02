package com.booking.book.memberbook.repository;

import com.booking.book.memberbook.domain.MemberBook;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MemberBookRepository extends ReactiveMongoRepository<MemberBook, Long> {

}
