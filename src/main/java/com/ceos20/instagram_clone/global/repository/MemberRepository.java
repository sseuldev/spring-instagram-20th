package com.ceos20.instagram_clone.global.repository;

import com.ceos20.instagram_clone.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByIdAndDeletedAtIsNull(Long memberId);

    Boolean existsMemberByNickname(String nickname);

    boolean existsMemberByEmail(String email);
}
