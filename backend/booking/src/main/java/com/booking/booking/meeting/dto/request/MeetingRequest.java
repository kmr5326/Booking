package com.booking.booking.meeting.dto.request;

import java.util.List;

public record MeetingRequest(
    String bookIsbn,

    String meetingTitle,

    String description,

    Integer maxParticipants,

    List<String> hashtagList
) {

}
