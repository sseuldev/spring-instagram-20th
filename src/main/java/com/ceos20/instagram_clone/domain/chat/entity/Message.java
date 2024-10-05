package com.ceos20.instagram_clone.domain.chat.entity;

import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id", nullable = false)
    private Long id;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    @Column(name = "send_time", columnDefinition = "timestamp")
    private LocalDateTime sendTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private Chatroom chatroom;

    @Builder
    public Message(String content, Member sender, Chatroom chatroom) {
        this.content = content;
        this.sender = sender;
        this.chatroom = chatroom;
        this.sendTime = LocalDateTime.now();
    }
}
