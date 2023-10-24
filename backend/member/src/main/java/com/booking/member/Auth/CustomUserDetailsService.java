package com.booking.member.Auth;

import com.booking.member.members.Member;
import com.booking.member.members.MemberRepository;
import com.booking.member.members.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("userDetailsService")
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws AuthenticationException {
        log.debug("CustomUserDetailsService");
        Member member = memberRepository.findByEmail(email);
        if (member != null) {
            log.warn("사용자 찾기 성공!");
            return createUser(member);
        } else {
            log.warn("사용자 찾기 실패!");
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
//        Optional<?> members=memberRepository.findById(walletAddress);
//        return memberRepository.findById(walletAddress).map(member->createUser(walletAddress,member))
//                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));


    }

    private org.springframework.security.core.userdetails.User createUser(Member member) {

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(UserRole.USER.name()));
        if(member.getRole().equals(UserRole.ADMIN)) {
            grantedAuthorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.name()));
        }

        return new org.springframework.security.core.userdetails.User(member.getEmail(),
                "",
                grantedAuthorities);
    }
}
