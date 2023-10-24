package com.booking.member.members;

import com.booking.member.members.dto.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

public interface MemberService {
    void signup(SignUpRequestDto req);

}
