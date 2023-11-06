//package com.booking.booking.participant.dto.response;
//
//import com.booking.booking.global.dto.MemberInfoResponse;
//import com.booking.booking.participant.domain.Participant;
//
//public record ParticipantResponse(
//        String loginId,
//        String nickname,
//        String profileImage,
//        Integer memberPk,
//        Boolean attendanceStatus,
//        Boolean paymentStatus
//) {
//    public ParticipantResponse(MemberInfoResponse memberInfoResponse, Participant participant) {
//        this(memberInfoResponse.loginId(), memberInfoResponse.nickname(), memberInfoResponse.profileImage(),
//                memberInfoResponse.memberPk(), participant.getAttendanceStatus(), participant.getPaymentStatus());
//    }
//}
