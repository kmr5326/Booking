package com.booking.member.members.service;

import com.booking.member.Auth.TokenDto;
import com.booking.member.Auth.TokenProvider;
import com.booking.member.members.Gender;
import com.booking.member.members.Member;
import com.booking.member.members.UserRole;
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
    private final TokenProvider tokenProvider;

    @Override
    @Transactional
    public Mono<String> signup(SignUpRequestDto req) {

        return Mono.justOrEmpty(memberRepository.findByLoginId(req.loginId()))
                .defaultIfEmpty(new Member())
                .flatMap(member -> {
                    if (checkMemberDuplicate(req.loginId())) {
                        return Mono.error(new RuntimeException("이미 가입된 회원입니다."));
                    }

                    String[] split=parseAddr(req.address());
                    Double lat=Double.parseDouble(split[0].trim());
                    Double lgt=Double.parseDouble(split[1].trim());

                    Member mem=Member.builder()
                            .age(req.age())
                            .email(req.email())
                            .gender(Gender.valueOf(req.gender()))
                            .loginId(req.loginId())
                            .nickname(req.nickname())
                            .fullName(req.fullName())
                            .lat(lat)
                            .lgt(lgt)
                            .role(UserRole.USER)
                            .profileImage(req.profileImage())
                            .provider(req.provider())
                            .point(0)
                            .build();

                    return Mono.fromRunnable(() ->  memberRepository.save(mem))
                            .subscribeOn(Schedulers.boundedElastic())
                            .then(Mono.just(mem));

                })
                .flatMap(member ->
                        Mono.fromCallable(()-> tokenProvider.createToken(member.getLoginId(), member.getId()))
                                .subscribeOn(Schedulers.boundedElastic())
                                .map(TokenDto::getAccessToken))
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
                member.getLat() == null ? -1 : member.getLat(),
                member.getLgt() == null ? -1 : member.getLgt(),
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

                    String[] split=parseAddr(req.address());
                    Double lat=Double.parseDouble(split[0].trim());
                    Double lgt=Double.parseDouble(split[1].trim());

                    member.setLat(lat);
                    member.setLgt(lgt);
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

    @Override
    public Mono<String> login(String loginId) {
        Member member=memberRepository.findByLoginId(loginId);
        if (member == null) {
            return Mono.error(new UsernameNotFoundException("회원 가입이 필요합니다."));
        }
        return Mono.fromCallable(() -> tokenProvider.createToken(loginId,member.getId()))
                .map(TokenDto::getAccessToken);
    }

    public boolean checkMemberDuplicate(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    public String[] parseAddr(String address){
        String addr=address.substring(14);
        addr=addr.substring(0,addr.indexOf("hAcc")).trim();
        return addr.split(",");
    }
}
