package com.booking.booking.meetinginfo.repository;

import com.booking.booking.meetinginfo.domain.MeetingInfo;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface MeetingInfoRepository extends R2dbcRepository<MeetingInfo, Long> {
    Flux<MeetingInfo> findAllByMeetingId(Long meetingId);
}
