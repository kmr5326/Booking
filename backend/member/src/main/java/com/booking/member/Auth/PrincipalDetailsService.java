package com.booking.member.Auth;

import com.booking.member.members.Member;
import com.booking.member.members.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);
        if(member==null){
            throw new UsernameNotFoundException("해당 유저를 찾을 수 없습니다.");
        }
//                .orElseThrow(() -> {
//                    return new UsernameNotFoundException("해당 유저를 찾을 수 없습니다.");
//                });
        return new PrincipalDetails(member,null);
    }
}
