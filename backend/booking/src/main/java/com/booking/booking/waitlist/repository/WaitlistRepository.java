package com.booking.booking.waitlist.repository;

import com.booking.booking.waitlist.domain.Waitlist;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface WaitlistRepository extends R2dbcRepository<Waitlist, Long> {
    Mono<Boolean> existsByMeetingIdAndMemberId(Long meetingId, Integer memberId);
    Mono<Long> deleteByMeetingIdAndMemberId(Long meetingId, Integer memberId);
//    List<Waitlist> findAllByMeetingMeetingId(Long meetingId);
}
