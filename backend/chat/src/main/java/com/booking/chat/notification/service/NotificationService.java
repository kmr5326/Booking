package com.booking.chat.notification.service;

import com.booking.chat.notification.domain.NotificationInformation;
import com.booking.chat.notification.dto.request.DeviceTokenInitRequest;
import com.booking.chat.notification.repository.NotificationInformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationInformationRepository notificationInformationRepository;

    public Mono<Void> upsertDeviceToken(Long memberId, DeviceTokenInitRequest deviceTokenInitRequest) {
        return notificationInformationRepository.findByMemberId(memberId)
                                                .flatMap(existingInfo -> {
                                                    // 정보가 이미 있으면 업데이트
                                                    existingInfo.update(deviceTokenInitRequest.deviceToken());
                                                    return notificationInformationRepository.save(existingInfo);
                                                })
                                                .switchIfEmpty(
                                                    // 정보가 없으면 새로 만들어 저장
                                                    Mono.defer(() -> notificationInformationRepository.save(
                                                        NotificationInformation.builder()
                                                                               .memberId(memberId)
                                                                               .deviceToken(deviceTokenInitRequest.deviceToken())
                                                                               .build()
                                                    ))
                                                )
                                                .then();
    }
}
