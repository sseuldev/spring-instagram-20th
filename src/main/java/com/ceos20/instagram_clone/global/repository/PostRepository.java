package com.ceos20.instagram_clone.global.repository;

import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByMember(Member member);

    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.images WHERE p.id = :postId")
    Optional<Post> findByIdWithImages(@Param("postId") Long postId);

    Optional<Post> findByIdAndDeletedAtIsNull(Long postId);
}
