package com.booking.member.follows;

import com.booking.member.members.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@RequiredArgsConstructor
@Table(name = "follows")
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
