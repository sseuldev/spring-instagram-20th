package com.ceos20.instagram_clone.domain.post.entity;

import com.ceos20.instagram_clone.domain.comment.entity.Comment;
import com.ceos20.instagram_clone.domain.hashtag.entity.Posthashtag;
import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false)
    private Long id;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    @Column(name = "comment_count")
    private int commentCount;

    @Column(length = 30)
    private String location;

    private String music;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Posthashtag> hashtags = new ArrayList<>();

    public void updateCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    @Builder
    public Post(String content, String location, String music, Member member) {
        this.content = content;
        this.commentCount = 0;
        this.location = location;
        this.music = music;
        this.member = member;
    }
}
