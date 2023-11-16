package com.booking.member.payments.domain;

import com.booking.member.members.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor
@Table(name = "payments")
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue
    @Column(name = "payments_id")
    private Integer id;

    private String tid;

    private LocalDateTime approved_at;

    private Integer amount;

    private PaymentType type;

    @ManyToOne
    @JoinColumn(name = "payer")
    private Member payer;

    @ManyToOne
    @JoinColumn(name = "receiver")
    private Member receiver;

    public static Payment paymentTypeSend(Member sender, Member receiver,Integer amount) {
        return Payment.builder()
                .approved_at(LocalDateTime.now())
                .amount(amount)
                .type(PaymentType.Send)
                .payer(sender)
                .receiver(receiver)
                .build();
    }

    public static Payment paymentTypeReceive(Member sender, Member receiver,Integer amount) {
        return Payment.builder()
                .approved_at(LocalDateTime.now())
                .amount(amount)
                .type(PaymentType.Receive)
                .payer(sender)
                .receiver(receiver)
                .build();
    }
}
