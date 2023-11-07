package com.booking.booking.hashtag.repository;

import com.booking.booking.hashtag.domain.Hashtag;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface HashtagRepository extends R2dbcRepository<Hashtag, Long> {
    Mono<Hashtag> findByContent(String content);
}
