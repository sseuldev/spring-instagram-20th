package com.ceos20.instagram_clone.domain.post.dto.response;

import com.ceos20.instagram_clone.domain.post.entity.Image;
import com.ceos20.instagram_clone.domain.post.entity.Post;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PostRes(

        Long postId,

        String content,

        String location,

        String music,

        Long memberId,

        List<String> imageUrls,

        int commentCount

) {
    public static PostRes createPostRes(Post post) {
        return PostRes.builder()
                .postId(post.getId())
                .content(post.getContent())
                .location(post.getLocation())
                .music(post.getMusic())
                .memberId(post.getMember().getId())
                .imageUrls(post.getImages().stream()
                        .map(Image::getImageUrl)
                        .collect(Collectors.toList()))
                .build();
    }

    public static PostRes getPostRes(Post post) {
        return PostRes.builder()
                .postId(post.getId())
                .content(post.getContent())
                .location(post.getLocation())
                .music(post.getMusic())
                .memberId(post.getMember().getId())
                .imageUrls(post.getImages().stream()
                        .map(Image::getImageUrl)
                        .collect(Collectors.toList()))
                .commentCount(post.getCommentCount())
                .build();
    }
}
