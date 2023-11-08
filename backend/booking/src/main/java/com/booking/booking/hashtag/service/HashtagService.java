package com.booking.booking.hashtag.service;

import com.booking.booking.hashtag.domain.Hashtag;
import com.booking.booking.hashtag.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    public Mono<Hashtag> findById(Long hashtagId) {
        log.info("Booking Server Hashtag - findById({})", hashtagId);
        return hashtagRepository.findById(hashtagId);
    }

    public Mono<Hashtag> findByContent(String content) {
        log.info("Booking Server Hashtag - findByContent({})", content);
        return hashtagRepository.findByContent(content);
    }

    public Mono<Hashtag> save(String content) {
        log.info("Booking Server Hashtag - save({})", content);
        return hashtagRepository.save(Hashtag.builder().content(content).build())
                .onErrorResume(error -> {
                    log.error("Booking Server Hashtag - Error during save : {}", error.toString());
                    return Mono.error(new RuntimeException("해시태그 저장 실패"));
                });
    }
}
