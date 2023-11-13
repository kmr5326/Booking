package com.booking.booking.global.dto.request;

public record EnrollNotificationRequest(
        Integer memberId,
        String meetingTitle
) {
}
