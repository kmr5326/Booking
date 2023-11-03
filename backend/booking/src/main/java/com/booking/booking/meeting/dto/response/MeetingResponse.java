package com.booking.booking.meeting.dto.response;

import com.booking.booking.hashtag.dto.response.HashtagResponse;
import com.booking.booking.meeting.domain.Meeting;

import java.util.List;

public record MeetingResponse (
        String bookIsbn,

        String meetingTitle,

        String description,

        Integer maxParticipants,

        List<HashtagResponse> hashtagList
) {
    public MeetingResponse(Meeting meeting, List<HashtagResponse> hashtagList) {
        this(meeting.getBookIsbn(), meeting.getMeetingTitle(), meeting.getDescription(), meeting.getMaxParticipants(),
                hashtagList);
    }
}
