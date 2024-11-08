package com.ceos20.instagram_clone.domain.hashtag.dto.request;

import com.ceos20.instagram_clone.domain.hashtag.entity.Hashtag;
import jakarta.validation.constraints.NotNull;

public record HashtagRequestDto(
        @NotNull String name
) {
    public Hashtag toEntity() {
        return Hashtag.builder()
                .name(this.name)
                .build();
    }
}
