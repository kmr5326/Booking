package com.booking.member.follows.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowingsResponseDto {
    private List<String> followings;
    private Integer followingsCnt;
}
