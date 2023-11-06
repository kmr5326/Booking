package com.booking.member.reports;

import com.booking.member.members.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@RequiredArgsConstructor
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue
    private Integer reportId;

    private String content;

    @ManyToOne
    @JoinColumn(name = "members_id")
    private Member member;
}
