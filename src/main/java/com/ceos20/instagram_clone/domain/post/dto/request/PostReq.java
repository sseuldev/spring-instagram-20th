package com.ceos20.instagram_clone.domain.post.dto.request;

import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.domain.post.entity.Image;
import com.ceos20.instagram_clone.domain.post.entity.Post;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

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
        public Post from(Member member) {
                return Post.builder()
                        .content(this.content)
                        .commentCount(this.commentCount)
                        .location(this.location)
                        .music(this.music)
                        .member(member)
                        .build();
        }
}
