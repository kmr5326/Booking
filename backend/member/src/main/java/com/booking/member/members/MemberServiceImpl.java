package com.booking.member.members;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private MemberRepository memberRepository;
    @Override
    public void signup() {

    }
}
