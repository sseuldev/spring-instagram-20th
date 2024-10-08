package com.ceos20.instagram_clone.global.repository;

import com.ceos20.instagram_clone.domain.post.entity.Postlike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostlikeRepository extends JpaRepository<Postlike, Long> {
   Optional<Postlike> findPostlikeByMemberIdAndPostId(Long memberId, Long postId);
}
