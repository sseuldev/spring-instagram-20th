package com.ceos20.instagram_clone.domain.member.dto.response;

import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.domain.member.entity.MemberStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MemberRes(

        @NotNull
        String name,

        @NotNull
        String nickname,

        @Email
        String email,

        String profileUrl,

        String linkUrl,

        String introduce,

        @NotNull
        MemberStatus status,

        LocalDateTime inactiveDate
) {
    public static MemberRes MemberEditRes(Member member) {
        return MemberRes.builder()
                .name(member.getName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileUrl(member.getProfileUrl())
                .linkUrl(member.getLinkUrl())
                .introduce(member.getIntroduce())
                .build();
    }

    public static MemberRes MemberInfoRes(Member member) {
        return MemberRes.builder()
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

