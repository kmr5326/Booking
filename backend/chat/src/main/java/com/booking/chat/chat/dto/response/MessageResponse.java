package com.booking.chat.chat.dto.response;

import com.booking.chat.chat.domain.Message;
import java.time.LocalDateTime;

public record MessageResponse(
    Long chatroomId,
    Long messageId,
    Long senderId,
    String content,
    Integer readCount,
    LocalDateTime timestamp
) {

    public MessageResponse(Message message) {
        this (
            message.getChatroomId(),
            message.getMessageId(),
            message.getMemberId(),
            message.getContent(),
            message.getReadCount(),
            message.getTimestamp()
        );
    }

}
