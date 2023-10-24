package com.booking.member.members;

import com.booking.member.members.dto.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private MemberRepository memberRepository;
    @Override
    public void signup(SignUpRequestDto req) {
        if(checkEmailDuplicate(req.email())){
            throw new RuntimeException("이미 가입된 이메일입니다.");
        };
        Member member= Member.builder()
                .email(req.provider()+"_"+req.email())
                .age(req.age())
                .gender(Gender.valueOf(req.gender()))
                .nickname(req.nickname())
                .fullName(req.fullName())
                .address(req.address())
                .role(UserRole.USER)
                .profileImage(req.profileImg())
                .build();
        memberRepository.save(member);
    }

    public boolean checkEmailDuplicate(String email) {
        return memberRepository.existsByEmail(email);
    }
}
