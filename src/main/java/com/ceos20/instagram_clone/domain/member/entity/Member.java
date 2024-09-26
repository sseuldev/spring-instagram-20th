package com.ceos20.instagram_clone.domain.member.entity;

import com.ceos20.instagram_clone.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 320)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String nickname;

    @Column(name = "profile_url", columnDefinition = "text")
    private String profileUrl;

    @Column(length = 200)
    private String introduce;

    @Column(name = "link_url", columnDefinition = "text")
    private String linkUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private MemberStatus status;

    @Column(name = "inactive_date", columnDefinition = "timestamp")
    private LocalDateTime inactiveDate;

    @Builder
    public Member(String name, String email, String password, String nickname, String profileUrl, String introduce, String linkUrl) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.introduce = introduce;
        this.linkUrl = linkUrl;
        this.status = MemberStatus.ACTIVE;
    }
}