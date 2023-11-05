package com.booking.booking.waitlist.service;

import com.booking.booking.meeting.domain.Meeting;
import com.booking.booking.waitlist.domain.Waitlist;
import com.booking.booking.waitlist.repository.WaitlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RequiredArgsConstructor
@Service
public class WaitlistService {
    private final WaitlistRepository waitlistRepository;

    public Mono<Boolean> existsByMeetingAndMemberId(Meeting meeting, Integer memberId) {
        log.info("Booking Server Waitlist - existsByMeetingAndMemberId({}, {})", meeting, memberId);
        return Mono
                .fromCallable(() -> waitlistRepository.existsByMeetingAndMemberId(meeting, memberId))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Void> enrollMeeting(Meeting meeting, Integer memberId) {
        return Mono.fromCallable(() ->
                        waitlistRepository.save(Waitlist.builder().meeting(meeting).memberId(memberId).build()))
                .subscribeOn(Schedulers.boundedElastic())
                .onErrorResume(error -> {
                    log.error("Booking Server Waitlist - Error during enrollMeeting : {}", error.getMessage());
                    return Mono.error(new RuntimeException("대기 목록 추가 실패"));
                })
                .then();
    }

    public Mono<Void> deleteByMeetingAndMemberId(Meeting meeting, Integer memberId) {
        return Mono.fromRunnable(() -> waitlistRepository.deleteByMeetingAndMemberId(meeting, memberId))
                .subscribeOn(Schedulers.boundedElastic())
                .onErrorResume(error -> {
                    log.error("Booking Server Waitlist - Error during deleteByMeetingAndMemberId : {}", error.getMessage());
                    return Mono.error(new RuntimeException("대기 목록 삭제 실패"));
                })
                .then();
    }
}
