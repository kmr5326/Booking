package com.booking.booking.global.dto.request;

import com.booking.booking.meeting.domain.Meeting;

public record JoinChatroomRequest(
        Long meetingId,
        Integer leaderId,
        String meetingTitle
) {
    public JoinChatroomRequest(Meeting meeting) {
        this(meeting.getMeetingId(), meeting.getLeaderId(), meeting.getMeetingTitle());
    }
}
