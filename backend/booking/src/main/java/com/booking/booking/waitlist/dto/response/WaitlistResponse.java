package com.booking.booking.waitlist.dto.response;

import com.booking.booking.global.dto.MemberInfoResponse;
import com.booking.booking.waitlist.domain.Waitlist;

public record WaitlistResponse(
        String loginId,
        String nickname,
        String profileImage,
        Integer memberPk
) {
    public WaitlistResponse(MemberInfoResponse memberInfoResponse, Waitlist waitlist) {
        this(memberInfoResponse.loginId(), memberInfoResponse.nickname(), memberInfoResponse.profileImage(),
                waitlist.getMemberId());
    }
}
