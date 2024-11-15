package com.ceos20.instagram_clone.domain.hashtag.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PosthashtagRequestDto(
        List<String> hashtagNames
) {
}
