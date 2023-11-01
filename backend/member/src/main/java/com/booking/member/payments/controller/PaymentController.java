package com.booking.member.payments.controller;

import com.booking.member.payments.Service.PaymentService;
import com.booking.member.payments.dto.ReadyPaymentRequestDto;
import com.booking.member.payments.dto.ReadyPaymentResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/payments")
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/ready")
    public Mono<ResponseEntity<ReadyPaymentResponseDto>> readyPayment(@RequestBody ReadyPaymentRequestDto req) {
        log.info("결제 준비 요청: {}", "");
        return paymentService.readyPayment(req)
                .flatMap(resp -> Mono.just(ResponseEntity.ok().body(resp)))
                .onErrorResume(e->{
                   log.error("결제 준비 요청 에러: {}",e.getMessage());
                   return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
                });
    }

    @PostMapping("/{paymentId}/approval")
    public void approve() {

    }

//    @GetMapping("/success")
//    public Mono<ResponseEntity<ReadyPaymentResponseDto>> readyPaymentSuccess(@RequestBody ReadyPaymentResponseDto response) {
//        return Mono.just(ResponseEntity.ok().body(response));
//    }
//
//    @GetMapping("/fail")
//    public Mono<ResponseEntity<String>> readyPaymentFail() {
//        return Mono.just(ResponseEntity.badRequest().body("결제 준비 요청 실패"));
//    }
//
//    @GetMapping("/cancle")
//    public Mono<ResponseEntity<String>> readyPaymentCancle() {
//        return Mono.just(ResponseEntity.ok().body("결제 준비 요청 취소"));
//    }
}
