package com.booking.member.follows.Repository;

import com.booking.member.follows.domain.Follow;
import com.booking.member.members.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow,Integer> {

    Follow findByFollowerAndFollowing(Member follower,Member following);

    //팔로워들 찾기
    List<Follow> findByFollowing(Member member);

    //팔로잉들 찾기
    List<Follow> findByFollower(Member member);
}
