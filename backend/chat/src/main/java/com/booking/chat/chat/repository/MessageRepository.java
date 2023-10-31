package com.booking.chat.chat.repository;

import com.booking.chat.chat.domain.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, Long> {

}
