package com.ceos20.instagram_clone.domain.member.dto.response;

import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.domain.member.entity.MemberStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MemberResponseDto(
        String name,
        String nickname,
        String email,
        String profileUrl,
        String linkUrl,
        String introduce,
        MemberStatus status,
        LocalDateTime inactiveDate
) {
    public static MemberResponseDto from(Member member) {
        return MemberResponseDto.builder()
                .name(member.getName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileUrl(member.getProfileUrl())
                .linkUrl(member.getLinkUrl())
                .introduce(member.getIntroduce())
                .status(member.getStatus())
                .inactiveDate(member.getInactiveDate())
                .build();
    }
}

