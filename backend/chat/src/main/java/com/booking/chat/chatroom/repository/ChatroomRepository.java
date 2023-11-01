package com.booking.chat.chatroom.repository;

import com.booking.chat.chatroom.domain.Chatroom;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ChatroomRepository extends ReactiveMongoRepository<Chatroom, Long> {

}
