package com.ceos20.instagram_clone.domain.post.entity;

import com.ceos20.instagram_clone.domain.comment.entity.Comment;
import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false)
    private Long id;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    @Column(name = "comment_count")
    @Builder.Default
    private int commentCount = 0;

    @Column(length = 30)
    private String location;

    private String music;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post")
    private List<Image> images = new ArrayList<>();
}
