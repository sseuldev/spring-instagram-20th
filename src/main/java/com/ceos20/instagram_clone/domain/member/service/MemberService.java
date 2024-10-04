package com.ceos20.instagram_clone.domain.member.service;

import com.ceos20.instagram_clone.domain.member.dto.request.MemberRequestDto;
import com.ceos20.instagram_clone.domain.member.dto.response.MemberResponseDto;
import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.global.exception.BadRequestException;
import com.ceos20.instagram_clone.global.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ceos20.instagram_clone.global.exception.ExceptionCode.NOT_FOUND_MEMBER_ID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));
    }

    /**
     * 회원 정보 조회
     * **/
    public MemberResponseDto getMemberInfo(Long memberId) {

        Member member = findMemberById(memberId);

        return MemberResponseDto.from(member);
    }

    /**
     * 프로필 편집 (회원 정보 수정)
     * **/
    @Transactional
    public MemberResponseDto updateMemberInfo(MemberRequestDto request, Long memberId) {

        Member member = findMemberById(memberId);

        member.update(request);
        Member saveMember = memberRepository.save(member);

        return MemberResponseDto.from(saveMember);
    }
}
