package com.ceos20.instagram_clone.domain.post.dto.response;

import com.ceos20.instagram_clone.domain.hashtag.dto.response.HashtagResponseDto;
import com.ceos20.instagram_clone.domain.hashtag.dto.response.PosthashtagResponseDto;
import com.ceos20.instagram_clone.domain.hashtag.entity.Hashtag;
import com.ceos20.instagram_clone.domain.hashtag.entity.Posthashtag;
import com.ceos20.instagram_clone.domain.post.entity.Image;
import com.ceos20.instagram_clone.domain.post.entity.Post;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PostResponseDto(

        Long postId,

        String content,

        String location,

        String music,

        Long memberId,

        List<String> imageUrls,

        int commentCount,

        List<HashtagResponseDto> hashtags

) {
    public static PostResponseDto from(Post post, List<Hashtag> hashtags) {

        List<HashtagResponseDto> posthashtags = hashtags == null ? null : hashtags.stream()
                .map(HashtagResponseDto::from)
                .toList();

        return PostResponseDto.builder()
                .postId(post.getId())
                .content(post.getContent())
                .location(post.getLocation())
                .music(post.getMusic())
                .memberId(post.getMember().getId())
                .imageUrls(post.getImages().stream()
                        .map(Image::getImageUrl)
                        .toList())
                .commentCount(post.getCommentCount())
                .hashtags(posthashtags)
                .build();
    }

    public static PostResponseDto from(Post post) {
        return PostResponseDto.builder()
                .postId(post.getId())
                .content(post.getContent())
                .location(post.getLocation())
                .music(post.getMusic())
                .memberId(post.getMember().getId())
                .imageUrls(post.getImages().stream()
                        .map(Image::getImageUrl)
                        .toList())
                .commentCount(post.getCommentCount())
                .build();
    }
}
