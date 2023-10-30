package com.booking.booking.meeting.dto.request;

import com.booking.booking.hashtag.domain.Hashtag;

import java.util.List;

public record MeetingRequest(
    String bookIsbn,

    String meetingTitle,

    String description,

    Integer maxParticipants,

    List<Hashtag> hashtagList
) {

}