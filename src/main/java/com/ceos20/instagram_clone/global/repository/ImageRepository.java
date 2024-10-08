package com.ceos20.instagram_clone.global.repository;

import com.ceos20.instagram_clone.domain.post.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
