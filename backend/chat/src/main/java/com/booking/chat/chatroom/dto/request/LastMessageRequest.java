package com.booking.chat.chatroom.dto.request;

import java.time.LocalDateTime;

public record LastMessageRequest(
    LocalDateTime lastMessageIndex
) {

}
