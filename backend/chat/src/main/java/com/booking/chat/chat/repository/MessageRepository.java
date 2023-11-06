package com.booking.chat.chat.repository;

import com.booking.chat.chat.domain.Message;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface MessageRepository extends ReactiveMongoRepository<Message, Long> {

    @Tailable
    @Query("{ chatroomId:  ?0}")
    Flux<Message> findByChatRoomId(Long roomId);
}
