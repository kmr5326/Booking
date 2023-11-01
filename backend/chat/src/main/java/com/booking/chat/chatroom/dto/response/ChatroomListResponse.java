package com.booking.chat.chatroom.dto.response;

import com.booking.chat.chatroom.domain.Chatroom;
import java.util.List;

public record ChatroomListResponse(
    Long chatroomId,
    List<Long> memberList
) {
    public static ChatroomListResponse from(Chatroom chatroom) {
        return new ChatroomListResponse(chatroom.get_id(), chatroom.getMemberList());
    }
}
