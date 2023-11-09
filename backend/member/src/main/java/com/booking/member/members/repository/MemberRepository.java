package com.booking.member.members.repository;

import com.booking.member.members.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Integer> {
    Member findByEmailAndProvider(String email,String provider);

    Member findByEmail(String email);
    Member findByLoginId(String loginId);
    Member findByNickname(String nickname);

    boolean existsByEmail(String email);
    boolean existsByLoginId(String loginId);

    boolean existsByNickname(String nickname);
}
