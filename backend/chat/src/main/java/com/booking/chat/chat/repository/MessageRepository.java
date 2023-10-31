package com.booking.chat.chat.repository;

import com.booking.chat.chat.domain.Message;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MessageRepository extends ReactiveMongoRepository<Message, Long> {

}
