package com.booking.booking.global.dto;

public record MemberInfoResponse(
    String loginId,
    String email,
    Integer age,
    String gender,
    String nickname,
    String fullname,
    Double lat,
    Double lgt,
    String profileImage,
    String provider,
    Integer memberPk
) {

}
