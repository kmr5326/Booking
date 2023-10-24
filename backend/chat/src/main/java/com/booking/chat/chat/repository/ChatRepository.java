package com.booking.chat.chat.repository;

import com.booking.chat.chat.domain.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<Chat, Long> {

}
