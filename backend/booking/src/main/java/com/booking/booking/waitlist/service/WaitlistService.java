package com.booking.booking.waitlist.service;

import com.booking.booking.global.utils.MemberUtil;
import com.booking.booking.waitlist.domain.Waitlist;
import com.booking.booking.waitlist.dto.response.WaitlistResponse;
import com.booking.booking.waitlist.repository.WaitlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class WaitlistService {
    private final WaitlistRepository waitlistRepository;

    public Mono<Boolean> existsByMeetingIdAndMemberId(Long meetingId, Integer memberId) {
        log.info("[Booking:Waitlist] existsByMeetingIdAndMemberId({}, {})", meetingId, memberId);

        return waitlistRepository.existsByMeetingIdAndMemberId(meetingId, memberId);
    }

    public Mono<Void> enrollMeeting(Long meetingId, Integer memberId) {
        log.info("[Booking:Waitlist] enrollMeeting({}, {})", meetingId, memberId);

        return waitlistRepository.save(Waitlist.builder().meetingId(meetingId).memberId(memberId).build())
                .onErrorResume(error -> {
                    log.info("[Booking:Waitlist ERROR] enrollMeeting : {}", error.getMessage());
                    return Mono.error(new RuntimeException("대기 목록 추가 실패"));
                })
                .then();
    }

    public Mono<Void> deleteByMeetingIdAndMemberId(Long meetingId, Integer memberId) {
        log.info("[Booking:Waitlist] deleteByMeetingIdAndMemberId({}, {})", meetingId, memberId);

        return waitlistRepository.deleteByMeetingIdAndMemberId(meetingId, memberId)
                .onErrorResume(error -> {
                    log.info("[Booking:Waitlist ERROR] deleteByMeetingIdAndMemberId : {}", error.getMessage());
                    return Mono.error(new RuntimeException("대기 목록 삭제 실패"));
                });
    }

    public Flux<WaitlistResponse> findAllByMeetingMeetingId(Long meetingId) {
        log.info("[Booking:Waitlist] findAllByMeetingMeetingId({})", meetingId);

        return waitlistRepository.findAllByMeetingId(meetingId)
                .flatMap(waitlist -> MemberUtil.getMemberInfoByPk(waitlist.getMemberId())
                                .flatMap(memberInfo -> Mono.just(new WaitlistResponse(memberInfo))))
                .onErrorResume(error -> {
                    log.info("[Booking:Waitlist ERROR] findAllByMeetingMeetingId : {}", error.getMessage());
                    return Flux.error(new RuntimeException("대기자 목록 조회 실패"));
                });
    }

    public Mono<Void> deleteAllByMeetingId(Long meetingId) {
        log.info("[Booking:Waitlist] deleteAllByMeetingId({})", meetingId);

        return waitlistRepository.deleteAllByMeetingId(meetingId)
                .onErrorResume(error -> {
                    log.info("[Booking:Waitlist ERROR] deleteAllByMeetingId : {}", error.getMessage());
                    return Mono.error(new RuntimeException("대기자 목록 삭제 실패"));
                });
    }
}
