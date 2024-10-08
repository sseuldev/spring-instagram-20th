package com.ceos20.instagram_clone.global.repository;

import com.ceos20.instagram_clone.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
