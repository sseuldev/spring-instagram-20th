package com.ceos20.instagram_clone.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record MemberReq(

        @NotNull
        String name,

        @NotNull
        String nickname,

        @Email
        String email,

        String profileUrl,

        String linkUrl,

        String introduce
) {
}