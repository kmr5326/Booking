package com.booking.member.payments.Service;

import com.booking.member.payments.dto.ReadyPaymentRequestDto;
import com.booking.member.payments.dto.ReadyPaymentResponseDto;
import reactor.core.publisher.Mono;

public interface PaymentService {

    Mono<ReadyPaymentResponseDto> readyPayment(ReadyPaymentRequestDto req);
}
