package com.ceos20.instagram_clone.domain.member.entity;

import com.ceos20.instagram_clone.domain.member.dto.request.MemberRequestDto;
import com.ceos20.instagram_clone.domain.post.entity.Post;
import com.ceos20.instagram_clone.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE member SET deleted_at = NOW() where id = ?")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 320)
    private String email;

    @Column(length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String nickname;

    @Column
    private String role;

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

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    public void update(MemberRequestDto memberReq) {
        this.name = memberReq.name();
        this.nickname = memberReq.nickname();
        this.email = memberReq.email();
        this.profileUrl = memberReq.profileUrl();
        this.linkUrl = memberReq.linkUrl();
        this.introduce = memberReq.introduce();
    }

    @Builder
    public Member(String name, String email, String role, String phoneNumber, String password, String nickname, String profileUrl, String introduce, String linkUrl, String status, LocalDateTime inactiveDate) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.introduce = introduce;
        this.linkUrl = linkUrl;
        this.status = (status == null || status.isEmpty()) ? MemberStatus.ACTIVE : MemberStatus.valueOf(status);
        this.inactiveDate = inactiveDate;
    }
}