package com.booking.booking.participant.dto.response;

import com.booking.booking.global.dto.response.MemberResponse;
import com.booking.booking.participant.domain.Participant;

public record ParticipantResponse(
        Integer memberPk,
        String loginId,
        String nickname,
        String profileImage,
        Boolean attendanceStatus,
        Boolean paymentStatus
) {
    public ParticipantResponse(MemberResponse member, Participant participant) {
        this(member.memberPk(), member.loginId(), member.nickname(), member.profileImage(),
                participant.getAttendanceStatus(), participant.getPaymentStatus());
    }
}
