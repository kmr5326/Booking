package com.booking.member.payments.domain;

import com.booking.member.members.Member;
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
    @JoinColumn(name="receiver")
    private Member receiver;
}
