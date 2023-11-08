package com.booking.chat.notification.dto.response;

public record NotificationResponse(
    String title,
    String body,
    String memberName,
    Long memberId
) {

}
