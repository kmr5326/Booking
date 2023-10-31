package com.booking.member.members.service;

import com.booking.member.members.Gender;
import com.booking.member.members.Member;
import com.booking.member.members.dto.MemberInfoResponseDto;
import com.booking.member.members.dto.ModifyRequestDto;
import com.booking.member.members.dto.SignUpRequestDto;
import com.booking.member.members.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Mono<Void> signup(SignUpRequestDto req) {

        return Mono.justOrEmpty(memberRepository.findByLoginId(req.loginId()))
                .defaultIfEmpty(new Member())
                .flatMap(member -> {
                    if (checkEmailDuplicate(req.email())) {
                        return Mono.error(new RuntimeException("이미 가입된 이메일입니다."));
                    }
                    member.setEmail(req.email());
                    member.setAge(req.age());
                    member.setGender(Gender.valueOf(req.gender()));
                    member.setNickname(req.nickname());
                    member.setFullName(req.fullName());
                    member.setAddress(req.address());

                    return Mono.fromRunnable(() -> memberRepository.save(member))
                            .subscribeOn(Schedulers.boundedElastic())
                            .then();

                })
                .onErrorResume(e -> {
                    log.error("회원 가입 에러: {}", e.getMessage());
                    return Mono.error(e);
                });
    }

    @Override
    public Mono<MemberInfoResponseDto> loadMemberInfo(String loginId) {
        Member member = memberRepository.findByLoginId(loginId);
        if (member == null) return Mono.error(new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        MemberInfoResponseDto memberInfoResponseDto = new MemberInfoResponseDto(
                member.getLoginId(),
                member.getEmail() == null ? "" : member.getEmail(),
                member.getAge() == null ? -1 : member.getAge(),
                member.getGender() == null ? "" : member.getGender().name(),
                member.getNickname(),
                member.getFullName() == null ? "" : member.getFullName(),
                member.getAddress() == null ? "" : member.getAddress(),
                member.getProfileImage(),
                member.getProvider()
        );
        return Mono.just(memberInfoResponseDto);
    }

    @Override
    @Transactional
    public Mono<Void> modifyMemberInfo(ModifyRequestDto req) {
        return Mono.defer(() -> {
                    Member member = memberRepository.findByLoginId(req.loginId());
                    if (member == null) return Mono.error(new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
                    member.setAddress(req.address());
                    member.setNickname(req.nickname());
                    member.setProfileImage(req.profileImage());

                    return Mono.fromRunnable(() -> memberRepository.save(member))
                            .subscribeOn(Schedulers.boundedElastic())
                            .then();
                })
                .then()
                .onErrorResume(e -> {
                    log.error("회원 수정 에러: {}", e.getMessage());
                    return Mono.error(e);
                });
    }

    @Override
    @Transactional
    public Mono<Void> deleteMember(String loginId) {
        return Mono.defer(() -> {
                    Member member = memberRepository.findByLoginId(loginId);
                    if (member == null) return Mono.error(new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
                    return Mono.fromRunnable(() -> memberRepository.delete(member))
                            .subscribeOn(Schedulers.boundedElastic())
                            .then();
                })
                .then()
                .onErrorResume(e -> {
                    log.error("회원 탈퇴 에러: {}", e.getMessage());
                    return Mono.error(e);
                });
    }

    public boolean checkEmailDuplicate(String email) {
        return memberRepository.existsByEmail(email);
    }
}
