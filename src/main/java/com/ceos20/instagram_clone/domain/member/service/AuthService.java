package com.ceos20.instagram_clone.domain.member.service;

import com.ceos20.instagram_clone.domain.member.dto.request.SignupRequestDto;
import com.ceos20.instagram_clone.domain.member.dto.response.MemberResponseDto;
import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.global.exception.BadRequestException;
import com.ceos20.instagram_clone.global.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ceos20.instagram_clone.global.exception.ExceptionCode.DUPLICATED_ADMIN_EMAIL;
import static com.ceos20.instagram_clone.global.exception.ExceptionCode.DUPLICATED_ADMIN_USERNAME;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 회원 가입
     * **/
    @Transactional
    public MemberResponseDto signup(SignupRequestDto request) {

        String name = request.name();
        String nickname = request.nickname();
        String password = request.password();
        String email = request.email();
        String phoneNumber = request.phoneNumber();

        if(memberRepository.existsMemberByNickname(nickname)){
            throw new BadRequestException(DUPLICATED_ADMIN_USERNAME);
        }

        if (email != null && memberRepository.existsMemberByEmail(email)) {
            throw new BadRequestException(DUPLICATED_ADMIN_EMAIL);
        }

        Member newMember = Member.builder()
                .name(name)
                .nickname(nickname)
                .password(bCryptPasswordEncoder.encode(password))
                .role("ROLE_ADMIN")
                .email(email)
                .phoneNumber(phoneNumber)
                .build();

        memberRepository.save(newMember);

        return MemberResponseDto.from(newMember);
    }


}
