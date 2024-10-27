package com.ceos20.instagram_clone.domain.hashtag.dto.response;

import com.ceos20.instagram_clone.domain.hashtag.entity.Hashtag;
import com.ceos20.instagram_clone.domain.post.entity.Post;
import lombok.Builder;

import java.util.List;

@Builder
public record PosthashtagResponseDto(
        Long postId,
        List<HashtagResponseDto> hashtags
) {
    public static PosthashtagResponseDto from(Post post, List<Hashtag> hashtags) {
        List<HashtagResponseDto> postHashtags = hashtags.stream()
                .map(HashtagResponseDto::from)
                .toList();

        return PosthashtagResponseDto.builder()
                .postId(post.getId())
                .hashtags(postHashtags)
                .build();
    }
}
