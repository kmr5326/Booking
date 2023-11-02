package com.booking.member.follows.Repository;

import com.booking.member.follows.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow,Integer> {
}
