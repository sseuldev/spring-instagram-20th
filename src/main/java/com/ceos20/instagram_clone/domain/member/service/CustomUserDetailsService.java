package com.ceos20.instagram_clone.domain.member.service;

import com.ceos20.instagram_clone.domain.member.dto.CustomUserDetails;
import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.global.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {

        // DB에서 조회
        Optional<Member> userDataOptional = memberRepository.findByNicknameAndDeletedAtIsNull(nickname);

        Member userData = userDataOptional.orElseThrow(() ->
                new UsernameNotFoundException("해당 유저를 찾을 수 없습니다 : " + nickname));

        // UserDetails에 담아서 return하면 AuthenticationManager가 검증함
        return new CustomUserDetails(userData);
    }
}
