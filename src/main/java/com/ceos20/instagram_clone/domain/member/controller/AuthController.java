package com.ceos20.instagram_clone.domain.member.controller;

import com.ceos20.instagram_clone.domain.member.dto.request.SignupRequestDto;
import com.ceos20.instagram_clone.domain.member.dto.response.MemberResponseDto;
import com.ceos20.instagram_clone.domain.member.service.AuthService;
import com.ceos20.instagram_clone.domain.member.service.MemberService;
import com.ceos20.instagram_clone.global.common.response.CommonResponse;
import com.ceos20.instagram_clone.global.common.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Security 관련 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "회원가입 요청 API")
    public CommonResponse<MemberResponseDto> signup(@Valid @RequestBody SignupRequestDto request) {

        return new CommonResponse<>(ResponseCode.SUCCESS, authService.signup(request));
    }

    @GetMapping("/admin")
    public String adminP() {

        return "admin Controller";
    }
}
