package com.booking.member.members;

import com.booking.member.members.dto.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private MemberService memberService;

    @PostMapping("/signup")
    Mono<ResponseEntity<String>> signup(@RequestBody Mono<SignUpRequestDto> reqMono) {
        return reqMono.flatMap(req -> {
            memberService.signup(req);
            return Mono.just(ResponseEntity.ok().body("signup success"));
        }).onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(e.getMessage())));
    }
}
