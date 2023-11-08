package com.booking.booking.meeting.dto.response;

import com.booking.booking.global.dto.response.BookResponse;
import com.booking.booking.hashtag.dto.response.HashtagResponse;
import com.booking.booking.meeting.domain.Meeting;

import java.util.List;

public record MeetingListResponse(
        Long meetingId,
        String bookIsbn,
        String bookTitle,
        String coverImage,
        String meetingTitle,
        Integer curParticipants,
        Integer maxParticipants,
        Double lat,
        Double lgt,
        List<HashtagResponse> hashtagList
) {
    public MeetingListResponse
            (Meeting meeting, BookResponse book, Integer curParticipants, List<HashtagResponse> hashtagList) {
        this(meeting.getMeetingId(), book.isbn(), book.title(), book.coverImage(),
                meeting.getMeetingTitle(), curParticipants, meeting.getMaxParticipants(),
                meeting.getLat(), meeting.getLgt(), hashtagList);
    }
}
