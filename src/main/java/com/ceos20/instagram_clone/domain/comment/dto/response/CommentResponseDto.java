package com.ceos20.instagram_clone.domain.comment.dto.response;

import com.ceos20.instagram_clone.domain.comment.entity.Comment;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CommentResponseDto(
        Long commentId,
        String content,
        int likes,
        Long postId,
        Long memberId,
        String nickname,
        Long parentCommentId,
        List<CommentResponseDto> replies
) {
    public static CommentResponseDto from(Comment comment) {
        return CommentResponseDto.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .likes(comment.getLikes())
                .postId(comment.getPost().getId())
                .memberId(comment.getMember().getId())
                .nickname(comment.getMember().getNickname())
                .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                .replies(comment.getReplies() != null ?
                        comment.getReplies().stream()
                                .map(reply -> from(reply))
                                .toList()
                        : new ArrayList<>())
                .build();
    }
}