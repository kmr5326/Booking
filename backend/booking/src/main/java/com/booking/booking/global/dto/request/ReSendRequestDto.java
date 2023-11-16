package com.booking.booking.global.dto.request;

public record ReSendRequestDto(
        Integer receiverMemberPk,
        Integer amount
) {
}
