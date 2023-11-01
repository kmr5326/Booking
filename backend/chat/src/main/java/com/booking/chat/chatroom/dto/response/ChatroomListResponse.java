package com.booking.chat.chatroom.dto.response;

import com.booking.chat.chatroom.domain.Chatroom;
import java.util.List;

public record ChatroomListResponse(
    Long meetingId,
    List<Chatroom> chatroomList
) {

}
