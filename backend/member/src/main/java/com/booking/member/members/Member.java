package com.booking.member.members;

import com.booking.member.follows.Follow;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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

    private String loginId;

    @Setter
    private String email;

    @Setter
    private Integer age;

    @Setter
    private Gender gender;

    @Setter
    @Column(length = 255)
    private String nickname;

    @Setter
    @Column(length = 255)
    private String fullName;

    //@Column(columnDefinition = "TEXT")
    @Setter
    @Column(length = 255)
    private String address;

    @Setter
    private UserRole role;

    @Setter
    private String profileImage;

    @OneToMany(mappedBy = "following")
    private List<Follow> followingMembers; // Members this user is following

    @OneToMany(mappedBy = "follower")
    private List<Follow> followerMembers;

    private String provider;

//    private String providerId;

    @ColumnDefault("0")
    private Integer point;
}
