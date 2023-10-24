package com.booking.chat.chat.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document(collection = "chats")
public class Chat {
    @Id
    private Long id;
    private Long chatroom_id;
    private String content;
    private String author;


}
