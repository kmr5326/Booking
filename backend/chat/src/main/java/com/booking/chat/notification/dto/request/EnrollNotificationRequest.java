package com.booking.chat.notification.dto.request;

public record EnrollNotificationRequest(
    Long memberId,
    String meetingTitle
) {

}
