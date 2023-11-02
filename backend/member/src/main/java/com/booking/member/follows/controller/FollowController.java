package com.booking.member.follows.controller;

import com.booking.member.follows.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
