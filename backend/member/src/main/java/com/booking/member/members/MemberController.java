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

    @PostMapping("/signup")
    Mono<ResponseEntity<?>> signup(@RequestBody Mono<SignUpRequestDto> reqMono){

        return Mono.just(ResponseEntity.noContent().build());
    }
}
