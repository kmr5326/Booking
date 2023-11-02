package com.booking.member.members.service;

import com.booking.member.members.dto.MemberInfoResponseDto;
import com.booking.member.members.dto.ModifyRequestDto;
import com.booking.member.members.dto.SignUpRequestDto;
import reactor.core.publisher.Mono;

public interface MemberService {
    Mono<String> signup(SignUpRequestDto req);
    Mono<MemberInfoResponseDto> loadMemberInfo(String loginId);
    Mono<Void> modifyMemberInfo(ModifyRequestDto req);
    Mono<Void> deleteMember(String loginId);

    Mono<String> login(String loginId);

}
