package com.ceos20.instagram_clone.domain.post.dto.request;

import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.domain.post.entity.Image;
import com.ceos20.instagram_clone.domain.post.entity.Post;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public record PostRequestDto(
        @NotNull
        String content,
        int commentCount,
        String location,
        String music,
        @NotNull
        List<String> imageUrls

) {
        public Post toEntity(Member member) {

                Post post = Post.builder()
                        .content(this.content)
                        .location(this.location)
                        .music(this.music)
                        .member(member)
                        .build();

                List<Image> images = this.imageUrls.stream()
                        .map(imageUrl -> Image.builder()
                                .imageUrl(imageUrl)
                                .post(post)  // 올바른 Post 엔티티 연결
                                .build())
                        .toList();

                post.getImages().addAll(images);

                return post;
        }
}
