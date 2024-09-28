package com.ceos20.instagram_clone.domain.post.dto.response;

import com.ceos20.instagram_clone.domain.post.entity.Postlike;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PostlikeRes (

        @NotNull
        Long postlikeId,

        @NotNull
        Long postId,

        @NotNull
        Long memberId
) {
}
