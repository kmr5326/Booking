package com.booking.booking.hashtag.service;

import com.booking.booking.hashtag.domain.Hashtag;
import com.booking.booking.hashtag.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    public Mono<Hashtag> findByContent(String content) {
        log.info("[Booking:Hashtag] findByContent({})", content);

        return hashtagRepository.findByContent(content);
    }

    public Mono<Hashtag> save(String content) {
        log.info("[Booking:Hashtag] save({})", content);

        return hashtagRepository.save(Hashtag.builder().content(content).build())
                .onErrorResume(error -> {
                    log.error("[Booking:Hashtag ERROR] save : {}", error.getMessage());
                    return Mono.error(new RuntimeException("해시태그 저장 실패"));
                });
    }

    public Flux<Hashtag> findHashtagsByMeetingId(Long meetingId) {
        log.info("[Booking:Hashtag] findHashtagsByMeetingId({})", meetingId);

        return hashtagRepository.findHashtagsByMeetingId(meetingId)
                .onErrorResume(error -> {
                    log.error("[Booking:Hashtag ERROR] findHashtagsByMeetingId : {}", error.getMessage());
                    return Mono.error(new RuntimeException("해시태그 목록 조회 실패"));
                });
    }
}
