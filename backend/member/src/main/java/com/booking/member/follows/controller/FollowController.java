package com.booking.member.follows.controller;

import com.booking.member.follows.dto.FollowersResponseDto;
import com.booking.member.follows.dto.FollowingsResponseDto;
import com.booking.member.follows.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/follows")
@Slf4j
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{nickname}")
    public Mono<ResponseEntity<String>> follow(@AuthenticationPrincipal UserDetails user,
                                          @PathVariable String nickname){
        log.info("팔로우 요청 {} 대상 {}",user.getUsername(),nickname);
        return followService.follow(user.getUsername(), nickname)
                .then(Mono.just(ResponseEntity.ok().body("")))
                .onErrorResume(e->{
                    log.error(e.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body(e.getMessage()));
                });
    }

    @DeleteMapping("/{nickname}")
    public Mono<ResponseEntity<String>> unfollow(@AuthenticationPrincipal UserDetails user,
                                             @PathVariable String nickname){
        log.info("언팔로우 요청 {} 대상 {}",user.getUsername(),nickname);
        return followService.unfollow(user.getUsername(), nickname)
                .then(Mono.just(ResponseEntity.ok().body("")))
                .onErrorResume(e->{
                    log.error(e.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body(e.getMessage()));
                });
    }

    @GetMapping("/followers/{nickname}")
    public Mono<ResponseEntity<FollowersResponseDto>> getFollowers(@PathVariable String nickname){
        log.info("팔로워 조회 : {}",nickname);
        return followService.getFollowers(nickname)
                .map(resp -> ResponseEntity.ok().body(resp));
    }

    @GetMapping("/followings/{nickname}")
    public Mono<ResponseEntity<FollowingsResponseDto>> getFollowings(@PathVariable String nickname){
        log.info("팔로잉 조회 : {}",nickname);
        return followService.getFollowings(nickname)
                .map(resp -> ResponseEntity.ok().body(resp));
    }
}
