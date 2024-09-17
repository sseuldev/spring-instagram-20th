package com.ceos20.instagram_clone.domain.member.entity;

import com.ceos20.instagram_clone.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    @Column(name = "profile_url")
    private String profileUrl;

    @Column(length = 200)
    private String introduce;

    @Column(name = "link_url")
    private String linkUrl;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Column(name = "inactive_date")
    private LocalDateTime inactiveDate;
}