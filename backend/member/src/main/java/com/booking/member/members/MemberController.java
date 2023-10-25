package com.booking.member.members;

import com.booking.member.members.dto.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
public class MemberController {

    private MemberService memberService;

    @PostMapping("/signup")
    Mono<ResponseEntity<String>> signup(@RequestBody Mono<SignUpRequestDto> reqMono) {
        return reqMono.flatMap(req -> {
            memberService.signup(req);
            return Mono.just(ResponseEntity.ok().body("signup success"));
        }).onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(e.getMessage())));
    }

    @GetMapping("/checkJwt/{token}")
    Mono<ResponseEntity<String>> checkJwt(@PathVariable String token){
      log.info("token={}",token);

      return Mono.just(ResponseEntity.ok().build());
    }

    @GetMapping("/test")
    Mono<ResponseEntity<String>> check(){

        return Mono.just(ResponseEntity.ok().body("hello flux"));
    }
}
