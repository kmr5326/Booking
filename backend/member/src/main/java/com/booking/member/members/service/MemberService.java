package com.booking.member.members.service;

import com.booking.member.members.dto.MemberInfoResponseDto;
import com.booking.member.members.dto.ModifyRequestDto;
import com.booking.member.members.dto.SignUpRequestDto;

public interface MemberService {
    void signup(SignUpRequestDto req);

    MemberInfoResponseDto loadMemberInfo(String loginId);

    void modifyMemberInfo(ModifyRequestDto req);

}
