package com.ceos20.instagram_clone.global.repository;

import com.ceos20.instagram_clone.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
