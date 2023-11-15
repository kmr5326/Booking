package com.booking.booking.meetinginfo.repository;

import com.booking.booking.meetinginfo.domain.MeetingInfo;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MeetingInfoRepository extends R2dbcRepository<MeetingInfo, Long> {
    Flux<MeetingInfo> findAllByMeetingId(Long meetingId);
    @Query("SELECT * FROM meetinginfos " +
            "WHERE meeting_id = :input_meeting_id " +
            "ORDER BY meetinginfo_id DESC LIMIT 1")
    Mono<MeetingInfo> findLatestByMeetingId(Long meetingId);
}
