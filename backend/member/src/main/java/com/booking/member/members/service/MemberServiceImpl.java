package com.booking.member.members.service;

import com.booking.member.members.Gender;
import com.booking.member.members.Member;
import com.booking.member.members.dto.MemberInfoResponseDto;
import com.booking.member.members.dto.ModifyRequestDto;
import com.booking.member.members.dto.SignUpRequestDto;
import com.booking.member.members.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void signup(SignUpRequestDto req) {
        if (checkEmailDuplicate(req.email())) {
            throw new RuntimeException("이미 가입된 이메일입니다.");
        }
        Member member = memberRepository.findByLoginId(req.loginId());
        member.setEmail(req.email());
        member.setAge(req.age());
        member.setGender(Gender.valueOf(req.gender()));
        member.setNickname(req.nickname());
        member.setFullName(req.fullName());
        member.setAddress(req.address());
        memberRepository.save(member);
    }

    @Override
    public MemberInfoResponseDto loadMemberInfo(String loginId) {
        Member member = memberRepository.findByLoginId(loginId);
        if (member == null) throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");

        return new MemberInfoResponseDto(
                member.getLoginId(),
                member.getEmail()==null?"":member.getEmail(),
                member.getAge()==null?-1: member.getAge(),
                member.getGender()==null?"":member.getGender().name(),
                member.getNickname(),
                member.getFullName()==null?"":member.getFullName(),
                member.getAddress()==null?"":member.getAddress(),
                member.getProfileImage(),
                member.getProvider()
        );
    }

    @Override
    public void modifyMemberInfo(ModifyRequestDto req) {
        Member member=memberRepository.findByLoginId(req.loginId());
        if (member == null) throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        member.setAddress(req.address());
        member.setNickname(req.nickname());
        member.setProfileImage(req.profileImage());
        memberRepository.save(member);
    }

    public boolean checkEmailDuplicate(String email) {
        return memberRepository.existsByEmail(email);
    }
}
