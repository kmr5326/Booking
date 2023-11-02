package com.booking.member.follows.service;

import com.booking.member.follows.Repository.FollowRepository;
import com.booking.member.follows.domain.Follow;
import com.booking.member.members.domain.Member;
import com.booking.member.members.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowServiceImpl implements FollowService{

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    @Override
    public Mono<Void> follow(String loginId, String targetNickname) {
        Member member=memberRepository.findByLoginId(loginId);
        Member target=memberRepository.findByNickname(targetNickname);
        if(target==null){
            log.error("팔로우 에러 대상 없음");
            return Mono.error(new UsernameNotFoundException("사용자 찾을 수 없음"));
        }
        Follow follow=Follow.builder()
                .follower(member)
                .following(target)
                .build();
        followRepository.save(follow);
        return Mono.empty();
    }

    @Override
    public Mono<Void> unfollow(String loginId, String targetNickname) {
        Member member=memberRepository.findByLoginId(loginId);
        Member target=memberRepository.findByNickname(targetNickname);
        if(target==null){
            log.error("언팔로우 에러 대상 없음");
            return Mono.error(new UsernameNotFoundException("사용자 찾을 수 없음"));
        }
        Follow follow=followRepository.findByFollowerAndFollowing(member,target);
        if(follow==null){
            log.error("팔로우 상태가 아닙니다.");
            return Mono.error(new RuntimeException("팔로우 상태가 아닙니다."));
        }
        followRepository.delete(follow);
        return Mono.empty();
    }
}
