package com.booking.member.members;

import com.booking.member.follows.Follow;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
@Table(name = "members")
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "members_id")
    private Integer id;

    private String email;

    private Integer age;

    private Gender gender;

    @Column(length = 255)
    private String nickname;

    @Column(length = 255)
    private String fullName;

    //@Column(columnDefinition = "TEXT")
    @Column(length = 255)
    private String address;

    private UserRole role;

    private String profileImage;

    @OneToMany(mappedBy = "following")
    private List<Follow> followingMembers; // Members this user is following

    @OneToMany(mappedBy = "follower")
    private List<Follow> followerMembers;

    private String provider;

//    private String providerId;
}
