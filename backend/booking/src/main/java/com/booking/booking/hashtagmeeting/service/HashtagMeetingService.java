//package com.booking.booking.hashtagmeeting.service;
//
//import com.booking.booking.hashtag.domain.Hashtag;
//import com.booking.booking.hashtag.service.HashtagService;
//import com.booking.booking.hashtagmeeting.domain.HashtagMeeting;
//import com.booking.booking.hashtagmeeting.repository.HashtagMeetingRepository;
//import com.booking.booking.meeting.domain.Meeting;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import reactor.core.scheduler.Schedulers;
//
//import java.util.List;
//
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class HashtagMeetingService {
//    private final HashtagMeetingRepository hashtagMeetingRepository;
//    private final HashtagService hashtagService;
//
//    public Mono<Void> saveHashtags(Meeting meeting, List<String> hashtagList) {
//        log.info("Booking Server HashtagMeeting - saveHashtags({}, {})", meeting.getMeetingTitle(), hashtagList);
//        return Flux.fromIterable(hashtagList)
//                .flatMap(content ->
//                    hashtagService.findByContent(content)
//                            .flatMap(optionalHashtag ->
//                                    optionalHashtag.map(Mono::just).orElseGet(() -> hashtagService.save(content)))
//                )
//                .flatMap(savedHashtag -> mapHashtagToMeeting(meeting, savedHashtag))
//                .onErrorResume(error -> {
//                    log.error("Booking Server HashtagMeeting - Error during saveHashtags : {}", error.toString());
//                    return Mono.error(error);
//                })
//                .then();
//    }
//
//    private Mono<Void> mapHashtagToMeeting(Meeting meeting, Hashtag hashtag) {
//        log.info("Booking Server HashtagMeeting - mapHashtagToMeeting({}, {})", meeting.getMeetingTitle(), hashtag.getContent());
//        return Mono
//                .fromRunnable(() -> hashtagMeetingRepository.save(
//                        HashtagMeeting.builder()
//                                .meeting(meeting)
//                                .hashtag(hashtag)
//                                .build()))
//                .subscribeOn(Schedulers.boundedElastic())
//                .onErrorResume(error -> {
//                    log.error("Booking Server HashtagMeeting - Error during mapHashtagToMeeting : {}", error.toString());
//                    return Mono.error(new RuntimeException("해시태그 미팅 연결 실패"));
//                })
//                .then();
//    }
//}
