package com.booking.chat.chatroom.service;

import com.booking.chat.chatroom.domain.Chatroom;
import com.booking.chat.chatroom.dto.request.InitChatroomRequest;
import com.booking.chat.chatroom.repository.ChatroomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;
    public Mono<Chatroom> initializeChatroom(InitChatroomRequest initChatroomRequest) {

        Chatroom chatroom = Chatroom.createWithLeader(initChatroomRequest);
        return chatroomRepository.save(chatroom);
    }
}
