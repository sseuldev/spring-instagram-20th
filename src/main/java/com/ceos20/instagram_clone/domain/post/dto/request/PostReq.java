package com.ceos20.instagram_clone.domain.post.dto.request;

import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.domain.post.entity.Image;
import com.ceos20.instagram_clone.domain.post.entity.Post;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record PostReq(

        @NotNull
        String content,

        int commentCount,

        String location,

        String music,

        @NotNull
        List<String> imageUrls

) {
        public Post toEntity(Member member) {

                List<Image> images = this.imageUrls.stream()
                        .map(imageUrl -> Image.builder()
                                .imageUrl(imageUrl)
                                .post(Post.builder().build())
                                .build())
                        .collect(Collectors.toList());

                return Post.builder()
                        .content(this.content)
                        .commentCount(this.commentCount)
                        .location(this.location)
                        .music(this.music)
                        .member(member)
                        .images(images)
                        .build();
        }
}
