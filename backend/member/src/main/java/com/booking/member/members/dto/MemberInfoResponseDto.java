package com.booking.member.members.dto;

public record MemberInfoResponseDto(
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
