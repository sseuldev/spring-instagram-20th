package com.ceos20.instagram_clone.domain.comment.controller;

import com.ceos20.instagram_clone.domain.comment.dto.request.CommentRequestDto;
import com.ceos20.instagram_clone.domain.comment.dto.response.CommentResponseDto;
import com.ceos20.instagram_clone.domain.comment.service.CommentService;
import com.ceos20.instagram_clone.domain.member.dto.CustomUserDetails;
import com.ceos20.instagram_clone.global.common.response.CommonResponse;
import com.ceos20.instagram_clone.global.common.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post/{postId}/comment")
@RequiredArgsConstructor
@Tag(name = "Comment", description = "댓글 관련 API")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = "특정 게시글에 댓글을 작성하는 API")
    @PostMapping
    public CommonResponse<CommentResponseDto> createComment(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody CommentRequestDto request) {

        return new CommonResponse<>(ResponseCode.SUCCESS, commentService.createComment(request, postId, userDetails.getMemberId()));
    }

    @Operation(summary = "게시글 댓글 조회", description = "특정 게시글의 전체 댓글을 조회하는 API")
    @GetMapping
    public CommonResponse<List<CommentResponseDto>> getAllComments(@PathVariable Long postId) {

        return new CommonResponse<>(ResponseCode.SUCCESS, commentService.getAllPostComment(postId));
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제하는 API")
    @DeleteMapping("/{commentId}")
    public CommonResponse<Void> deleteComment(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal CustomUserDetails userDetails) {

        commentService.deleteComment(commentId, postId, userDetails.getMemberId());
        return new CommonResponse<>(ResponseCode.SUCCESS);
    }
}
