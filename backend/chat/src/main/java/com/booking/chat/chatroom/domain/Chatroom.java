package com.booking.chat.chatroom.domain;

import com.booking.chat.chatroom.dto.request.InitChatroomRequest;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Document(collection = "chatroom")
public class Chatroom {

    @Transient
    public Long messageIndex = 1L;

    @Id
    private Long _id;

    private Long leaderId;

    private List<Long> memberList;

    private String meetingTitle;

    private String lastMessage;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime lastMessageReceivedTime;

    //TODO : lastMessage를 보여주면 좋을 듯 / 이벤트리스너 & 테일러블


    public static Chatroom createWithLeader(InitChatroomRequest initChatroomRequest) {
        return Chatroom.builder()
                       ._id(initChatroomRequest.meetingId())
                       .leaderId(initChatroomRequest.leaderId())
                       .memberList(List.of(initChatroomRequest.leaderId()))
                       .meetingTitle(initChatroomRequest.meetingTitle())
                       .lastMessageReceivedTime(LocalDateTime.now())
                       .build();
    }

    public void updateIndex() {
        this.messageIndex++;
    }

    public void updateListMessageReceived() {
        this.lastMessageReceivedTime = LocalDateTime.now();
    }

    public void updateLastMessage(String msg) {
        this.lastMessage = msg;
    }
}
