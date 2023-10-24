package com.booking.chat.chat.repository;

import com.booking.chat.chat.domain.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<Message, Long> {

}
