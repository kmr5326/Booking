package com.booking.booking.hashtag.service;

import com.booking.booking.hashtag.domain.Hashtag;
import com.booking.booking.hashtag.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    public Mono<Optional<Hashtag>> findByContent(String content) {
        return Mono
                .fromSupplier(() -> hashtagRepository.findByContent(content))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Hashtag> save(String content) {
        log.info("Booking Server - '{}' request saveHashtag", content);
        return Mono
                .fromSupplier(() -> hashtagRepository.save(
                        Hashtag.builder()
                                .content(content)
                                .build()
                ))
                .subscribeOn(Schedulers.boundedElastic());
    }
//
//    public Mono<Hashtag> test(String content) {
//        return Mono.justOrEmpty(hashtagRepository.findByContent(content)).subscribeOn(Schedulers.boundedElastic());
//    }
}
