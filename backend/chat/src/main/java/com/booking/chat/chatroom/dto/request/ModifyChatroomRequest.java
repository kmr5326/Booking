package com.booking.chat.chatroom.dto.request;

public record ModifyChatroomRequest(
    Long meetingId,
    String meetingTitle
) {

}
