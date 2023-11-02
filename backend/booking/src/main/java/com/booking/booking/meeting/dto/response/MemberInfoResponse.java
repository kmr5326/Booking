package com.booking.booking.meeting.dto.response;

public record MemberInfoResponse(
    String loginId,
    String email,
    Integer age,
    String gender,
    String nickname,
    String fullname,
    String address,
    String profileImage,
    String provider
) {

}
