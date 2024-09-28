package com.ceos20.instagram_clone.domain.comment.dto.request;

import com.ceos20.instagram_clone.domain.comment.entity.Comment;
import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.domain.post.entity.Post;
import jakarta.validation.constraints.NotNull;

public record CommentReq (
        @NotNull Long postId,

        @NotNull Long memberId,

        @NotNull
        String content,

        Long parentCommentId
) {
        public Comment toEntity(Post post, Member member, Comment parentComment) {
                return Comment.builder()
                        .post(post)
                        .member(member)
                        .content(this.content)
                        .parentComment(parentComment)
                        .build();
        }
}
