package com.ceos20.instagram_clone.domain.hashtag.entity;

import com.ceos20.instagram_clone.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hashtag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id", nullable = false)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(nullable = false)
    private int hashtagCount;

    public void increaseCount() {
        this.hashtagCount++;
    }

    @Builder
    public Hashtag(String name) {
        this.name = name;
        this.hashtagCount = 0;
    }
}
