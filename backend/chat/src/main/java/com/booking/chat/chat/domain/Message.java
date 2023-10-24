package com.booking.chat.chat.domain;

import java.util.Date;
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

    @Id private Long message_id;
    private Long chatroom_id;
    private Long member_id;

    private String content;

    @CreatedDate
    private Date createdAt;


}
