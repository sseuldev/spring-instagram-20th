package com.ceos20.instagram_clone.domain.member.service;

import com.ceos20.instagram_clone.domain.member.dto.request.SignupRequestDto;
import com.ceos20.instagram_clone.domain.member.dto.response.MemberResponseDto;
import com.ceos20.instagram_clone.domain.member.dto.response.TokenResponseDto;
import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.domain.member.entity.Refresh;
import com.ceos20.instagram_clone.global.exception.BadRequestException;
import com.ceos20.instagram_clone.global.jwt.JWTUtil;
import com.ceos20.instagram_clone.global.repository.MemberRepository;
import com.ceos20.instagram_clone.global.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

import static com.ceos20.instagram_clone.global.exception.ExceptionCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    /**
     * 회원 가입
     * **/
    @Transactional
    public MemberResponseDto signup(SignupRequestDto request) {

        String nickname = request.nickname();
        String password = request.password();
        String email = request.email();

        if(memberRepository.existsMemberByNickname(nickname)){
            throw new BadRequestException(DUPLICATED_ADMIN_USERNAME);
        }
        if (email != null && memberRepository.existsMemberByEmail(email)) {
            throw new BadRequestException(DUPLICATED_ADMIN_EMAIL);
        }

        Member newMember = request.toEntity(bCryptPasswordEncoder.encode(password));

        memberRepository.save(newMember);

        return MemberResponseDto.from(newMember);
    }

    /**
     * Refresh Token 추출
     * **/
    public String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    return cookie.getValue();
                }
            }
        }
        throw new BadRequestException(NOT_FOUND_REFRESH_TOKEN);
    }

    /**
     * Refresh Token 검증
     * **/
    public void validateRefreshToken(String refreshToken) {

        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new BadRequestException(EXPIRED_PERIOD_REFRESH_TOKEN);
        }

        String category = jwtUtil.getCategory(refreshToken);
        if (!category.equals("refresh")) {
            throw new BadRequestException(INVALID_REFRESH_TOKEN);
        }

        Boolean isExist = refreshRepository.existsByRefresh(refreshToken);
        if (!isExist) {
            throw new BadRequestException(INVALID_REFRESH_TOKEN);
        }
    }

    /**
     * Access Token 재발급
     * **/
    public String reissueAccessToken(String refreshToken) {

        String nickname = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);
        return jwtUtil.createJwt("access", nickname, role, 1000L * 60 * 60 * 2);
    }

    /**
     * 새로운 Refresh Token 생성
     * **/
    @Transactional
    public Cookie createRefreshTokenCookie(String refreshToken) {

        String nickname = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);
        String newRefresh = jwtUtil.createJwt("refresh", nickname, role, 1000L * 60 * 60 * 24 * 14);

        if (nickname == null) {
            throw new BadRequestException(FAIL_TO_VALIDATE_TOKEN);
        }

        deleteAndSaveNewRefreshToken(nickname, newRefresh, 1000L * 60 * 60 * 24 * 14);

        return createCookie("refresh", newRefresh);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(1000 * 60 * 60 * 24 * 14);
        cookie.setHttpOnly(true);
         cookie.setPath("/");
        // cookie.setSecure(true);

        return cookie;
    }

    /**
     * 기존의 Refresh Token 삭제 후 새 Refresh Token 저장
     **/
    @Transactional
    public void deleteAndSaveNewRefreshToken(String nickname, String newRefresh, Long expiredMs) {

        refreshRepository.deleteByRefresh(newRefresh);

        addRefreshEntity(nickname, newRefresh, expiredMs);
    }

    /**
     * 새로운 Refresh Token 저장하는 메서드
     **/
    @Transactional
    public void addRefreshEntity(String nickname, String refresh, Long expiredMs) {

        Date expirationDate = new Date(System.currentTimeMillis() + expiredMs);

        Refresh refreshEntity = Refresh.builder()
                .nickname(nickname)
                .refresh(refresh)
                .expiration(expirationDate.toString())
                .build();

        refreshRepository.save(refreshEntity);
    }

    /**
     * 응답 헤더 및 쿠키 설정
     * **/
    public void setNewTokens(HttpServletResponse response, String newAccessToken, Cookie refreshCookie) {

        response.setHeader("Authorization", "Bearer " + newAccessToken);

        response.addCookie(refreshCookie);
    }
}
