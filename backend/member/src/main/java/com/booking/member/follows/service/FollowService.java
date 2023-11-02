package com.booking.member.follows.service;

import reactor.core.publisher.Mono;

public interface FollowService {
    Mono<Void> follow(String loginId,String targetNickname);
}
