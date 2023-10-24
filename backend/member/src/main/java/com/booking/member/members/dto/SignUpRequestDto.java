package com.booking.member.members.dto;

public record SignUpRequestDto(String email,
                               Integer age,
                               String gender,
                               String nickname,
                               String) {
}
