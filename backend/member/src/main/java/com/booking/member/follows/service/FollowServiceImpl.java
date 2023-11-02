package com.booking.member.follows.service;

import com.booking.member.follows.Repository.FollowRepository;
import com.booking.member.follows.domain.Follow;
import com.booking.member.follows.dto.FollowersResponseDto;
import com.booking.member.follows.dto.FollowingsResponseDto;
import com.booking.member.members.domain.Member;
import com.booking.member.members.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

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
        if(followRepository.findByFollowerAndFollowing(member,target)!=null){
            log.error("이미 팔로우 상태입니다.");
            return Mono.error(new RuntimeException("이미 팔로우 상태입니다."));
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

    @Override
    public Mono<FollowersResponseDto> getFollowers(String nickname) {
        Member member=memberRepository.findByNickname(nickname);
        if(member==null)return Mono.error(new UsernameNotFoundException("사용자 찾을 수 없음"));
        List<Follow> followers=followRepository.findByFollowing(member);
        List<String> followerNicknames = followers.stream()
                .map(follow -> follow.getFollower().getNickname())
                .collect(Collectors.toList());

        FollowersResponseDto responseDto = FollowersResponseDto.builder()
                .followers(followerNicknames)
                .followersCnt(followerNicknames.size())
                .build();

        return Mono.just(responseDto);
    }

    @Override
    public Mono<FollowingsResponseDto> getFollowings(String nickname) {
        Member member=memberRepository.findByNickname(nickname);
        if(member==null)return Mono.error(new UsernameNotFoundException("사용자 찾을 수 없음"));
        List<Follow> followings=followRepository.findByFollower(member);
        List<String> followingsNicknames = followings.stream()
                .map(follow -> follow.getFollowing().getNickname())
                .toList();

        FollowingsResponseDto responseDto = FollowingsResponseDto.builder()
                .followings(followingsNicknames)
                .followingsCnt(followingsNicknames.size())
                .build();

        return Mono.just(responseDto);
    }
}
