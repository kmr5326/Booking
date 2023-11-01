package com.booking.member.payments.Service;

import com.booking.member.payments.dto.ApprovalResponseDto;
import com.booking.member.payments.dto.ReadyPaymentRequestDto;
import com.booking.member.payments.dto.ReadyPaymentResponseDto;
import reactor.core.publisher.Mono;

public interface PaymentService {

    Mono<ReadyPaymentResponseDto> readyPayment(ReadyPaymentRequestDto req,String loginId);

    Mono<ApprovalResponseDto> approvePayment(String pgToken);
}
