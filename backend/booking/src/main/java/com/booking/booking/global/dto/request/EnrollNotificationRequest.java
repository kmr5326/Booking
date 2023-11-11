package com.booking.booking.global.dto.request;

public record EnrollNotificationRequest(
        Long memberId,
        String meetingTitle
) {
}
