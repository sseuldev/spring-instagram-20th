package com.ceos20.instagram_clone.domain.post.controller;

import com.ceos20.instagram_clone.domain.member.dto.CustomUserDetails;
import com.ceos20.instagram_clone.domain.post.dto.request.PostRequestDto;
import com.ceos20.instagram_clone.domain.post.dto.response.PostResponseDto;
import com.ceos20.instagram_clone.domain.post.service.PostService;
import com.ceos20.instagram_clone.domain.post.service.PostlikeService;
import com.ceos20.instagram_clone.global.common.response.CommonResponse;
import com.ceos20.instagram_clone.global.common.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO: memberId 바꾸기

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
@Tag(name = "Post", description = "지원 관련 API")
public class PostController {

    private final PostService postService;
    private final PostlikeService postlikeService;

    @Operation(summary = "게시글 조회", description = "하나의 게시글을 조회하는 API")
    @GetMapping("/{postId}")
    public CommonResponse<PostResponseDto> getPost(@PathVariable Long postId) {

        return new CommonResponse<>(ResponseCode.SUCCESS, postService.getPost(postId));
    }

    @Operation(summary = "게시글 전체 조회", description = "내가 작성한 전체 게시글을 조회하는 API")
    @GetMapping("/my")
    public CommonResponse<List<PostResponseDto>> getAllPosts(@AuthenticationPrincipal CustomUserDetails userDetails) {

        return new CommonResponse<>(ResponseCode.SUCCESS, postService.getAllPosts(userDetails.getMemberId()));
    }

    @Operation(summary = "게시글 작성", description = "게시글을 작성하는 API")
    @PostMapping
    public CommonResponse<PostResponseDto> createPost(@Valid @RequestBody PostRequestDto request, @AuthenticationPrincipal CustomUserDetails userDetails) {

        return new CommonResponse<>(ResponseCode.SUCCESS, postService.createPost(request, userDetails.getMemberId()));
    }

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제하는 API")
    @DeleteMapping("/{postId}")
    public CommonResponse<Void> deletePost(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails userDetails) {

        postService.deletePost(postId);
        return new CommonResponse<>(ResponseCode.SUCCESS);
    }

    @Operation(summary = "게시글 좋아요", description = "게시글에 좋아요를 추가하는 API")
    @PostMapping("/{postId}/like")
    public CommonResponse<Void> likePost(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails userDetails) {

        postlikeService.likePost(postId, userDetails.getMemberId());
        return new CommonResponse<>(ResponseCode.SUCCESS);
    }

    @Operation(summary = "게시글 좋아요 삭제", description = "게시글의 좋아요를 삭제하는 API")
    @DeleteMapping("/{postId}/like")
    public CommonResponse<Void> cancelPostLike(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails userDetails) {

        postlikeService.cancelPostlike(postId, userDetails.getMemberId());
        return new CommonResponse<>(ResponseCode.SUCCESS);
    }
}
