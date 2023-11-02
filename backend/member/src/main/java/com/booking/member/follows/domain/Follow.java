package com.booking.member.follows.domain;

import com.booking.member.members.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@RequiredArgsConstructor
@Table(name = "follows")
@Builder
public class Follow {
    @Id
    @GeneratedValue
    @Column(name = "follows_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "following_id")
    private Member following;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private Member follower;
}
