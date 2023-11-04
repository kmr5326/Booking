package com.booking.member.follows.service;

import com.booking.member.follows.dto.FollowersResponseDto;
import com.booking.member.follows.dto.FollowingsResponseDto;
import reactor.core.publisher.Mono;

public interface FollowService {
    Mono<Void> follow(String loginId,String targetNickname);

    Mono<Void> unfollow(String loginId,String targetNickname);

    Mono<FollowersResponseDto> getFollowers(String nickname);

    Mono<FollowingsResponseDto> getFollowings(String nickname);
}
