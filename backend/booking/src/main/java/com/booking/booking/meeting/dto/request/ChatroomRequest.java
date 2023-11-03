package com.booking.booking.meeting.dto.request;

import com.booking.booking.meeting.domain.Meeting;

public record ChatroomRequest(
        Long meetingId,
        Integer memberPk,
        String meetingTitle
) {
    public ChatroomRequest(Meeting meeting) {
        this(meeting.getMeetingId(), meeting.getLeaderId(), meeting.getMeetingTitle());
    }
}
