package com.ceos20.instagram_clone.domain.member.controller;

import com.ceos20.instagram_clone.domain.member.dto.CustomUserDetails;
import com.ceos20.instagram_clone.domain.member.dto.response.MemberResponseDto;
import com.ceos20.instagram_clone.domain.member.service.MemberService;
import com.ceos20.instagram_clone.global.common.response.CommonResponse;
import com.ceos20.instagram_clone.global.common.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    @Operation(summary = "메인화면 기본 정보 조회", description = "메인 홈 화면 기본 회원 정보 보여주기 API")
    public CommonResponse<MemberResponseDto> memberInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMemberId();
        System.out.println("memberId: " + memberId);
        return new CommonResponse<>(ResponseCode.SUCCESS, memberService.getMemberInfo(memberId));
    }
}
