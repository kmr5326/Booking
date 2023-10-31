package com.booking.booking.chatroom.service;

import com.booking.booking.chatroom.domain.Chatroom;
import com.booking.booking.chatroom.repository.ChatroomRepository;
import com.booking.booking.meeting.domain.Meeting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatroomService {
    private final ChatroomRepository chatroomRepository;

    public Mono<Void> createChatroom(Meeting meeting) {
        return Mono.fromRunnable(() -> chatroomRepository.save(buildChatroom(meeting)))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    private Chatroom buildChatroom(Meeting meeting) {
        return Chatroom.builder()
                .meeting(meeting)
                .build();
    }
}
