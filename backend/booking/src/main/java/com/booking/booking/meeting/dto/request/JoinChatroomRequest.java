package com.booking.booking.meeting.dto.request;

public record JoinChatroomRequest(
        Long meetingId,
        Integer memberId
) {
}
