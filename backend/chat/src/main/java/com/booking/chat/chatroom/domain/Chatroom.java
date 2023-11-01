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
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Document(collection = "chatroom")
public class Chatroom {

    @Id
    private Long _id;

    private Long leaderId;

    private List<Long> memberList;

    @CreatedDate
    private LocalDateTime createdAt;

    public static Chatroom createWithLeader(InitChatroomRequest initChatroomRequest) {
        return Chatroom.builder()
                       ._id(initChatroomRequest.meetingId())
                       .leaderId(initChatroomRequest.leaderId())
                       .memberList(List.of(initChatroomRequest.leaderId()))
                       .build();
    }
}
