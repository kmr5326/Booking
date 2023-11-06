package com.booking.booking.meeting.dto.response;

import com.booking.booking.meeting.domain.Meeting;

public record MeetingResponse (
        Long meetingId,
        Integer leaderId,
        String bookIsbn,
        String meetingTitle,
        String description,
        Integer maxParticipants
//        List<HashtagResponse> hashtagList
) {
//    public MeetingResponse(Meeting meeting, List<HashtagResponse> hashtagList) {
//        this(meeting.getMeetingId(), meeting.getLeaderId(), meeting.getBookIsbn(), meeting.getMeetingTitle(),
//                meeting.getDescription(), meeting.getMaxParticipants(), hashtagList);
//    }
    public MeetingResponse(Meeting meeting) {
        this(meeting.getMeetingId(), meeting.getLeaderId(), meeting.getBookIsbn(), meeting.getMeetingTitle(),
                meeting.getDescription(), meeting.getMaxParticipants());
    }

    public Meeting toEntity() {
        return Meeting.builder()
                .meetingId(meetingId)
                .bookIsbn(bookIsbn)
                .meetingTitle(meetingTitle)
                .description(description)
                .maxParticipants(maxParticipants)
                .build();
    }
}
