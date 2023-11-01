package com.booking.member.payments.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor
@Table(name = "payments")
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    private String tid;

    private LocalDateTime approved_at;

    private Integer amount;

    private PaymentType type;

}
