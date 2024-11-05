package com.ceos20.instagram_clone.domain.member.dto.request;

import jakarta.validation.constraints.NotNull;

public record LoginRequestDto(

        @NotNull
        String nickname,
        @NotNull
        String password
) {
}
