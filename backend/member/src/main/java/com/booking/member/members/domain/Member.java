package com.booking.member.members.domain;

import com.booking.member.follows.domain.Follow;
import com.booking.member.payments.domain.Payment;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.ws.rs.DefaultValue;
import java.util.ArrayList;
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
//    @Setter
//    @Column(length = 255)
//    private String address;
    @Column(name = "latitude")
    @Setter
    @DefaultValue("0")
    private Double lat;

    @Column(name = "longitude")
    @Setter
    @DefaultValue("0")
    private Double lgt;

    @Setter
    private UserRole role;

    @Setter
    private String profileImage;

    @OneToMany(mappedBy = "following",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followingMembers=new ArrayList<>(); // Members this user is following

    @OneToMany(mappedBy = "follower",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followerMembers=new ArrayList<>();

    private String provider;

//    private String providerId;

    @ColumnDefault("0")
    @Setter
    private Integer point;

    @OneToMany(mappedBy = "payer",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments=new ArrayList<>();

    @OneToMany(mappedBy = "receiver",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> receivers=new ArrayList<>();
}
