package com.booking.booking.hashtagmeeting.repository;

import com.booking.booking.hashtagmeeting.domain.HashtagMeeting;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface HashtagMeetingRepository extends R2dbcRepository<HashtagMeeting, Long> {
}
