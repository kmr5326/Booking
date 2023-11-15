package com.booking.chat.chatroom.dto.response;

import com.booking.chat.chatroom.domain.Chatroom;
import com.booking.chat.message.domain.Message;
import java.util.List;

public record ChatroomListResponse(
    Long chatroomId,
    String lastMessageIdx,
    String meetingTitle,
    String lastMessage,
    String coverImage,
    List<Long> memberList

) {
    public static ChatroomListResponse from(Chatroom chatroom, Message message) {
        return new ChatroomListResponse(chatroom.get_id(), message.get_id(), chatroom.getMeetingTitle(), message.getContent(), chatroom.getCoverImage(), chatroom.getMemberList());
    }

    public static ChatroomListResponse fromNewChatroom(Chatroom chatroom) {
        return new ChatroomListResponse(chatroom.get_id(), null, chatroom.getMeetingTitle(), null, chatroom.getCoverImage(), chatroom.getMemberList());
    }
}
