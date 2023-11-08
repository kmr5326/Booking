package com.booking.booking.hashtagmeeting.repository;

import com.booking.booking.hashtagmeeting.domain.HashtagMeeting;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;

public interface HashtagMeetingRepository extends R2dbcRepository<HashtagMeeting, Long> {
    @Query("SELECT DISTINCT hashtag_id FROM hashtag_meeting WHERE meeting_id = :meetingId")
    Flux<Long> findAllByMeetingId(@Param("meetingId") Long meetingId);

    @Query("SELECT DISTINCT meeting_id FROM hashtag_meeting WHERE hashtag_id = :hashtagId")
    Flux<Long> findAllByHashtagId(@Param("hashtagId") Long hashtagId);
}
