package com.booking.member.Auth;

import com.booking.member.members.Member;
import com.booking.member.members.MemberRepository;
import com.booking.member.members.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}", oAuth2User.getAttributes());

        //getAttributes : {sub=103058387739722400130, name=안창범, given_name=창범, family_name=안,
        // picture=https://lh3.googleusercontent.com/a/AEdFTp5SiCyTaOLog9sDPN6QhWwsUj7xPbfj4HQF0fdC=s96-c,
        // email=chb20050@gmail.com, email_verified=true, locale=ko}
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getAttribute("sub");
        String loginId = provider + "_" +providerId;
        String profileImg=oAuth2User.getAttribute("picture").toString();
        String fullName=oAuth2User.getAttribute("family_name").toString()+oAuth2User.getAttribute("given_name").toString();

        Member memberData = memberRepository.findByEmail(loginId);
        Member member;

        if(memberData==null) {
            member = Member.builder()
                    .email(loginId)
                    .nickname(oAuth2User.getAttribute("name"))
                    .provider(provider)
                    .providerId(providerId)
                    .role(UserRole.USER)
                    .profileImage(profileImg)
                    .fullName(fullName)
                    .build();
            memberRepository.save(member);
        } else {
            member=memberData;
        }

        return new PrincipalDetails(member, oAuth2User.getAttributes());
    }
}