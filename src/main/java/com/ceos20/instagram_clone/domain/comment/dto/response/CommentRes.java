package com.ceos20.instagram_clone.domain.comment.dto.response;

import com.ceos20.instagram_clone.domain.comment.entity.Comment;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record CommentRes(
        Long commentId,
        String content,
        int likes,
        Long postId,
        Long memberId,

        String nickname,

        Long parentCommentId,

        List<CommentRes> replies
) {
    public static CommentRes createCommentRes(Comment comment) {
        return CommentRes.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .likes(comment.getLikes())
                .postId(comment.getPost().getId())
                .memberId(comment.getMember().getId())
                .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                .replies(new ArrayList<>())
                .build();
    }

    public static CommentRes getCommentRes(Comment comment, List<CommentRes> replies) {
        return CommentRes.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .likes(comment.getLikes())
                .postId(comment.getPost().getId())
                .memberId(comment.getMember().getId())
                .nickname(comment.getMember().getNickname())
                .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)  // 부모 댓글 ID 설정
                .replies(replies)
                .build();
    }
}
