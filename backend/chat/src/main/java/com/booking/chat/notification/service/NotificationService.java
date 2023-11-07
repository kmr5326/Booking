package com.booking.chat.notification.service;

import com.booking.chat.notification.repository.NotificationInformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationInformationRepository notificationInformationRepository;
}
