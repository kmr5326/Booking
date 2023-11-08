package com.booking.chat.notification.dto.request;

public record NotificationRequest(
    Long memberId,
    String title,
    String content
) {

}
