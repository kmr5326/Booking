package com.booking.booking.waitlist.service;

import com.booking.booking.waitlist.domain.Waitlist;
import com.booking.booking.waitlist.repository.WaitlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class WaitlistService {
    private final WaitlistRepository waitlistRepository;

    public Mono<Boolean> existsByMeetingIdAndMemberId(Long meetingId, Integer memberId) {
        log.info("Booking Server Waitlist - existsByMeetingIdAndMemberId({}, {})", meetingId, memberId);

        return waitlistRepository.existsByMeetingIdAndMemberId(meetingId, memberId);
    }

    public Mono<Void> enrollMeeting(Long meetingId, Integer memberId) {
        log.info("Booking Server Waitlist - enrollMeeting({}, {})", meetingId, memberId);

        return waitlistRepository.save(Waitlist.builder().meetingId(meetingId).memberId(memberId).build())
                .onErrorResume(error -> {
                    log.error("Booking Server Waitlist - Error during enrollMeeting : {}", error.getMessage());
                    return Mono.error(new RuntimeException("대기 목록 추가 실패"));
                })
                .then();
    }

//    public Mono<Void> deleteByMeetingAndMemberId(Meeting meeting, Integer memberId) {
//        log.info("Booking Server Waitlist - deleteByMeetingAndMemberId({}, {})", meeting, memberId);
//
//        return Mono.fromRunnable(() -> waitlistRepository.deleteByMeetingAndMemberId(meeting, memberId))
//                .subscribeOn(Schedulers.boundedElastic())
//                .onErrorResume(error -> {
//                    log.error("Booking Server Waitlist - Error during deleteByMeetingAndMemberId : {}", error.getMessage());
//                    return Mono.error(new RuntimeException("대기 목록 삭제 실패"));
//                })
//                .then();
//    }
//
//    public Flux<WaitlistResponse> findAllByMeetingMeetingId(Long meetingId) {
//        log.info("Booking Server Waitlist - findAllByMeetingMeetingId({})", meetingId);
//
//        return Mono.fromCallable(() -> waitlistRepository.findAllByMeetingMeetingId(meetingId))
//                .flatMapMany(Flux::fromIterable)
//                .flatMap(waitlist -> MemberUtil.getMemberInfoByPk(waitlist.getMemberId())
//                                .flatMap(memberInfo -> Mono.just(new WaitlistResponse(memberInfo, waitlist))))
//                .onErrorResume(error -> {
//                    log.error("Booking Server Waitlist - Error during findAllByMeetingId : {}", error.getMessage());
//                    return Flux.error(new RuntimeException("대기자 목록 조회 실패"));
//                })
//                .subscribeOn(Schedulers.boundedElastic());
//    }
}
