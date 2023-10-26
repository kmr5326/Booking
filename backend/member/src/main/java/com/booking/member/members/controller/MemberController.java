package com.booking.member.members.controller;

import com.booking.member.members.dto.DeleteMemberRequestDto;
import com.booking.member.members.dto.MemberInfoResponseDto;
import com.booking.member.members.dto.ModifyRequestDto;
import com.booking.member.members.dto.SignUpRequestDto;
import com.booking.member.members.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    Mono<ResponseEntity<String>> signup(@RequestBody SignUpRequestDto req) {
        log.info("회원 가입 요청={}", req);
        memberService.signup(req);
        return Mono.just(ResponseEntity.ok().body("signup success"))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(e.getMessage())));
    }

    @GetMapping("/memberInfo/{loginId}")
    Mono<ResponseEntity<MemberInfoResponseDto>> loadMember(@PathVariable String loginId){
        log.info("유저 정보 조회 loginId={}",loginId);
        try{
            MemberInfoResponseDto memberInfo=memberService.loadMemberInfo(loginId);
            return Mono.just(ResponseEntity.ok().body(memberInfo));
        }
        catch (Exception e){
//            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
            return Mono.error(e);
            // 에러 메세지 어떻게 보내지?
        }
    }

    @GetMapping("/afterLogin/{token}")
    Mono<ResponseEntity<String>> loadMemberAfterLogin(@PathVariable String token) {
        try {
            return Mono.just(ResponseEntity.ok().body(token));
        } catch (Exception e) {
//            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
            return Mono.error(e);
        }
    }

    @PatchMapping("/modification")
    Mono<ResponseEntity<String>> modifyMemberInfo(@RequestBody ModifyRequestDto req) {
        log.info("회원 정보 수정 요청={}", req);
        memberService.modifyMemberInfo(req);
        return Mono.just(ResponseEntity.ok().body("유저 정보 수정 완료"))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(e.getMessage())));

    }

    @DeleteMapping("/deletion")
    Mono<ResponseEntity<String>> deleteMember(@RequestBody DeleteMemberRequestDto req){
        log.info("회원 탈퇴 {}",req);
        memberService.deleteMember(req.loginId());
        return Mono.just(ResponseEntity.ok().body("회원 탈퇴 성공"))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(e.getMessage())));
    }
}
