package com.ceos20.instagram_clone.domain.hashtag.dto.response;

import com.ceos20.instagram_clone.domain.hashtag.entity.Hashtag;
import lombok.Builder;

@Builder
public record HashtagResponseDto(
        Long hashtagId,
        String name,
        int count
) {
    public static HashtagResponseDto from(Hashtag hashtag) {
        return HashtagResponseDto.builder()
                .hashtagId(hashtag.getId())
                .name(hashtag.getName())
                .count(hashtag.getHashtagCount())
                .build();
    }
}
