package com.booking.chat.chat.domain;

import java.time.LocalDateTime;
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
@Document(collection = "chats")
public class Message {

    @Id
    private String _id;

    private Long chatroomId;

    private Long messageId;

    private Long memberId;

    private String content;

    @CreatedDate
    private LocalDateTime timestamp;


    public void setAutoIncrementId() {}

}
