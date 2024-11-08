package com.ceos20.instagram_clone.domain.member.controller;

import com.ceos20.instagram_clone.domain.member.dto.CustomUserDetails;
import com.ceos20.instagram_clone.domain.member.dto.request.MemberRequestDto;
import com.ceos20.instagram_clone.domain.member.dto.response.MemberResponseDto;
import com.ceos20.instagram_clone.domain.member.service.MemberService;
import com.ceos20.instagram_clone.global.common.response.CommonResponse;
import com.ceos20.instagram_clone.global.common.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Tag(name = "Member", description = "멤버 관련 API")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    @Operation(summary = "회원 정보 조회", description = "회원의 기본 정보를 조회하는 API")
    public CommonResponse<MemberResponseDto> memberInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMemberId();
        return new CommonResponse<>(ResponseCode.SUCCESS, memberService.getMemberInfo(memberId));
    }

    @PutMapping
    @Operation(summary = "회원 정보 수정", description = "회원의 프로필 정보를 수정하는 API")
    public CommonResponse<MemberResponseDto> updateMemberInfo(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody MemberRequestDto request) {

        return new CommonResponse<>(ResponseCode.SUCCESS, memberService.updateMemberInfo(request, userDetails.getMemberId()));
    }
}
