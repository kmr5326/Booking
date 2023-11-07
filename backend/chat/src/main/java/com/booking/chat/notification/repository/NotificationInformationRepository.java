package com.booking.chat.notification.repository;

import com.booking.chat.notification.domain.NotificationInformation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface NotificationInformationRepository extends ReactiveMongoRepository<NotificationInformation, Long> {

}
