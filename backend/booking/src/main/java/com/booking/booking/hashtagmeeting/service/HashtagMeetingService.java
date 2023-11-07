package com.booking.booking.hashtagmeeting.service;

import com.booking.booking.hashtag.domain.Hashtag;
import com.booking.booking.hashtag.service.HashtagService;
import com.booking.booking.hashtagmeeting.domain.HashtagMeeting;
import com.booking.booking.hashtagmeeting.repository.HashtagMeetingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class HashtagMeetingService {
    private final HashtagMeetingRepository hashtagMeetingRepository;
    private final HashtagService hashtagService;

    public Mono<Void> saveHashtags(Long meetingId, List<String> hashtagList) {
        log.info("Booking Server HashtagMeeting - saveHashtags({}, {})", meetingId, hashtagList);

        return Flux.fromIterable(hashtagList)
                .flatMap(content -> hashtagService.findByContent(content).switchIfEmpty(hashtagService.save(content)))
                .flatMap(hashtag -> mapHashtagToMeeting(meetingId, hashtag))
                .onErrorResume(error -> {
                    log.error("Booking Server HashtagMeeting - Error during saveHashtags : {}", error.toString());
                    return Mono.error(error);
                })
                .then();
    }

    private Mono<Void> mapHashtagToMeeting(Long meetingId, Hashtag hashtag) {
        log.info("Booking Server HashtagMeeting - mapHashtagToMeeting({}, {})", meetingId, hashtag.getContent());

        return hashtagMeetingRepository.save(HashtagMeeting.builder().meetingId(meetingId).hashtagId(hashtag.getHashtagId()).build())
                .onErrorResume(error -> {
                    log.error("Booking Server HashtagMeeting - Error during mapHashtagToMeeting : {}", error.toString());
                    return Mono.error(new RuntimeException("해시태그 미팅 연결 실패"));
                })
                .then();
    }

    public Flux<Hashtag> findHashtagByMeetingId(Long meetingId) {
        log.info("Booking Server HashtagMeeting - findHashtagByMeetingId({})", meetingId);

        return hashtagMeetingRepository.findAllByMeetingId(meetingId)
                .flatMap(hashtagService::findById)
                .onErrorResume(error -> {
                    log.error("Booking Server HashtagMeeting - Error during findHashtagByMeetingId : {}", error.toString());
                    return Mono.error(new RuntimeException("해시태그 목록 조회 실패"));
                });
    }

    public Flux<Long> findMeetingIdByHashtagId(Long hashtagId) {
        log.info("Booking Server HashtagMeeting - findMeetingByHashtagId({})", hashtagId);

        return hashtagMeetingRepository.findAllByHashtagId(hashtagId)
                .onErrorResume(error -> {
                    log.error("Booking Server HashtagMeeting - Error during findMeetingIdByHashtagId : {}", error.toString());
                    return Mono.error(new RuntimeException("해시태그 - 모임 검색 실패"));
                });
    }
}
