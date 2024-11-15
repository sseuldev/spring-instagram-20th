package com.ceos20.instagram_clone.global.repository;

import com.ceos20.instagram_clone.domain.hashtag.entity.Posthashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PosthashtagRepository extends JpaRepository<Posthashtag, Long> {

    List<Posthashtag> findByPostId(Long postId);
}
