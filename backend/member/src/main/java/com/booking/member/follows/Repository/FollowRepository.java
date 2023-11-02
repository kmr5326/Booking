package com.booking.member.follows.Repository;

import com.booking.member.follows.domain.Follow;
import com.booking.member.members.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow,Integer> {

    Follow findByFollowerAndFollowing(Member follower,Member following);
}
