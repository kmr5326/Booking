package com.booking.chat.chat.domain;

import java.util.Date;
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
@Document(collection = "chats")
public class Message {

    @Transient
    public static final String SEQUENCE_NAME = "message_sequence";

    @Id
    private Long _id;

    private Long chatroomId;

    private Long memberId;

    private String content;

    @CreatedDate
    private Date timestamp;


}
