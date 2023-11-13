package com.booking.booking.meeting.dto.request;

public record MeetingAttendRequest(
        Long meetingId,
        Long meetinginfoId,
        Double lat,
        Double lgt
) {
}
