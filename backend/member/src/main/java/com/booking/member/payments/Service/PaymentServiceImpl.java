package com.booking.member.payments.Service;

import com.booking.member.payments.dto.ReadyPaymentRequestDto;
import com.booking.member.payments.dto.ReadyPaymentResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Value("${kakaopay.admin.key}")
    private String key;

    @Value("${kakaopay.approval.url}")
    private String approvalUrl;

    @Value("${kakaopay.fail.url}")
    private String failUrl;

    @Value("${kakaopay.cancle.url}")
    private String cancleUrl;


    @Override
    public Mono<ReadyPaymentResponseDto> readyPayment(ReadyPaymentRequestDto req) {
        final WebClient webClient = WebClient.create("https://kapi.kakao.com");

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("cid", "TC0ONETIME");
        formData.add("partner_order_id", "POI");
        formData.add("partner_user_id", "PUI");
        formData.add("item_name", "point");
        formData.add("quantity", "1");
        formData.add("total_amount", String.valueOf(req.amount()));
        formData.add("tax_free_amount", "0");
        formData.add("approval_url", approvalUrl);
        formData.add("cancel_url", cancleUrl);
        formData.add("fail_url", failUrl);

        return webClient.post()
                .uri("/v1/payment/ready")
                .header("Authorization", "KakaoAK " + key)
                .header("Content-Type","application/x-www-form-urlencoded")
//                .headers(headers -> headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED))
                .bodyValue(formData)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class).doOnNext(body -> {
                        // 로그에 오류 본문을 출력
                        log.error("4xx error: {}", body);
                    }).then(Mono.error(new RuntimeException("Client error")));
                })
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class).doOnNext(body -> {
                        // 로그에 오류 본문을 출력
                        log.error("5xx error: {}", body);
                    }).then(Mono.error(new RuntimeException("Server error")));
                })
                .bodyToMono(ReadyPaymentResponseDto.class);
    }
}
