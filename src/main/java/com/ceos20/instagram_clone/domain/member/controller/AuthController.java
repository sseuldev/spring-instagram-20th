package com.ceos20.instagram_clone.domain.member.controller;

import com.ceos20.instagram_clone.domain.member.dto.request.SignupRequestDto;
import com.ceos20.instagram_clone.domain.member.dto.response.MemberResponseDto;
import com.ceos20.instagram_clone.domain.member.dto.response.TokenResponseDto;
import com.ceos20.instagram_clone.domain.member.service.AuthService;
import com.ceos20.instagram_clone.domain.member.service.MemberService;
import com.ceos20.instagram_clone.global.common.response.CommonResponse;
import com.ceos20.instagram_clone.global.common.response.ResponseCode;
import com.ceos20.instagram_clone.global.jwt.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Security 관련 API")
public class AuthController {

    private final AuthService authService;
    private final JWTUtil jwtUtil;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "회원가입 요청 API")
    public CommonResponse<MemberResponseDto> signup(@Valid @RequestBody SignupRequestDto request) {

        return new CommonResponse<>(ResponseCode.SUCCESS, authService.signup(request));
    }

    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발급", description = "토큰 재발급 요청 API")
    public CommonResponse<Void> reissue(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = authService.extractRefreshToken(request);
        authService.validateRefreshToken(refreshToken);

        String newAccessToken = authService.reissueAccessToken(refreshToken);
        Cookie RefreshTokenCookie = authService.createRefreshTokenCookie(refreshToken);

        authService.setNewTokens(response, newAccessToken, RefreshTokenCookie);

        return new CommonResponse<>(ResponseCode.SUCCESS);
    }

    @GetMapping("/admin")
    @Operation(summary = "관리자 권한", description = "어드민 페이지 API")
    public String adminP() {

        return "Admin Controller 입니다";
    }
}
