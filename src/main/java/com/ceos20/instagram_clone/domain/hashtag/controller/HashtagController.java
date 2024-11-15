package com.ceos20.instagram_clone.domain.hashtag.controller;

import com.ceos20.instagram_clone.domain.hashtag.dto.request.PosthashtagRequestDto;
import com.ceos20.instagram_clone.domain.hashtag.dto.response.HashtagResponseDto;
import com.ceos20.instagram_clone.domain.hashtag.dto.response.PosthashtagResponseDto;
import com.ceos20.instagram_clone.domain.hashtag.service.HashtagService;
import com.ceos20.instagram_clone.global.common.response.CommonResponse;
import com.ceos20.instagram_clone.global.common.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hashtag")
@RequiredArgsConstructor
@Tag(name = "Hashtag", description = "해시태그 관련 API")
public class HashtagController {

    private final HashtagService hashtagService;

    @Operation(summary = "게시글 해시태그 추가", description = "특정 게시글에 해시태그 생성 또는 가져와서 추가하는 API")
    @PostMapping("/{postId}")
    public CommonResponse<PosthashtagResponseDto> addHashtagsToPost(@PathVariable Long postId, @RequestBody PosthashtagRequestDto request) {

        return new CommonResponse<>(ResponseCode.SUCCESS, hashtagService.addHashtagsToPost(postId, request), "게시글 해시태그 추가를 성공하였습니다");
    }
}
