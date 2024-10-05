package com.ceos20.instagram_clone.domain.chat.dto.request;

import com.ceos20.instagram_clone.domain.chat.entity.Chatroom;
import com.ceos20.instagram_clone.domain.chat.entity.Message;
import com.ceos20.instagram_clone.domain.member.entity.Member;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MessageRequestDto(
        @NotNull Long chatroomId,
        @NotNull Long senderId,
        @NotNull String content
) {
    public Message toEntity(Member sender, Chatroom chatroom) {
        return Message.builder()
                .content(content)
                .sender(sender)
                .chatroom(chatroom)
                .build();
    }
}
