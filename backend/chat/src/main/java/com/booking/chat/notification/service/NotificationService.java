package com.booking.chat.notification.service;

import com.booking.chat.notification.domain.NotificationInformation;
import com.booking.chat.notification.dto.request.DeviceTokenInitRequest;
import com.booking.chat.notification.dto.response.NotificationResponse;
import com.booking.chat.notification.repository.NotificationInformationRepository;
import com.google.api.core.ApiFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationInformationRepository notificationInformationRepository;

    public Mono<Void> upsertDeviceToken(Long memberId, DeviceTokenInitRequest deviceTokenInitRequest) {
        return notificationInformationRepository.findByMemberId(memberId)
                                                .flatMap(existingInfo -> {
                                                    // 정보가 이미 있으면 업데이트
                                                    existingInfo.update(
                                                        deviceTokenInitRequest.deviceToken());
                                                    return notificationInformationRepository.save(
                                                        existingInfo);
                                                })
                                                .switchIfEmpty(
                                                    // 정보가 없으면 새로 만들어 저장
                                                    Mono.defer(
                                                        () -> notificationInformationRepository.save(
                                                            NotificationInformation.builder()
                                                                                   ._id(
                                                                                       UUID.randomUUID())
                                                                                   .memberId(
                                                                                       memberId)
                                                                                   .deviceToken(
                                                                                       deviceTokenInitRequest.deviceToken())
                                                                                   .build()
                                                        ))
                                                )
                                                .then();
    }

    public Mono<Void> sendChattingNotification(Long memberId) {
        return notificationInformationRepository.findByMemberId(memberId)
                                                .flatMap(info -> {
                                                    log.info(" notification send to {} member ",
                                                        memberId);

                                                    Notification notification = Notification.builder()
                                                                                            .setBody("안녕하세요")
                                                                                            .setTitle("메세지 왔어요")
                                                                                            .build();

                                                    Message message = Message.builder()
                                                                             .setNotification(notification)
                                                                             .setToken(info.getDeviceToken())
                                                                             .build();

                                                    return send(message);
                                                })
                                                .then();
    }

    public Mono<Void> sendChattingNotification(NotificationResponse notificationResponse) {
        return notificationInformationRepository.findByMemberId(notificationResponse.memberId())
                                                .flatMap(info -> {
                                                    log.info("Notification send to {} member", notificationResponse.memberId());
//                                                    Notification notification = Notification.builder()
//                                                                                            .setBody(notificationResponse.body())
//                                                                                            .setTitle("%s\n%s".formatted(notificationResponse.title(),notificationResponse.memberName()))
//                                                                                            .build();
//
//                                                    Message message = Message.builder()
//                                                                             .setNotification(notification)
//                                                                             .setToken(info.getDeviceToken())
//                                                                             .build();

                                                    return send(Message.builder()
                                                                       .setNotification(Notification.builder()
                                                                                                    .setBody(notificationResponse.body())
                                                                                                    .setTitle("%s\n%s".formatted(notificationResponse.title(),notificationResponse.memberName()))
                                                                                                    .build())
                                                                       .setToken(info.getDeviceToken())
                                                                       .build()).then();
                                                })
                                                .then();
    }

    public Mono<Void> send(Message message) {
        ApiFuture<String> apiFuture = FirebaseMessaging.getInstance()
                                                       .sendAsync(message);

        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        apiFuture.addListener(() -> {
            try {
                // ApiFuture가 성공적으로 완료되면 CompletableFuture도 완료
                completableFuture.complete(apiFuture.get());
            } catch (Exception e) {
                // ApiFuture에서 예외가 발생하면 CompletableFuture도 예외로 마크
                completableFuture.completeExceptionally(e);
            }
        }, MoreExecutors.directExecutor()); // directExecutor는 현재 스레드에서 리스너를 실행합니다.

        return Mono.fromFuture(completableFuture)
                   .then();
    }
}
