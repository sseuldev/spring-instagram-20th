package com.ceos20.instagram_clone.domain.chat.dto.request;

import com.ceos20.instagram_clone.domain.chat.entity.Chatroom;
import com.ceos20.instagram_clone.domain.member.entity.Member;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ChatroomRequestDto(
        @NotNull Long senderId,
        @NotNull Long receiverId
) {
    public Chatroom toEntity(Member sender, Member receiver) {
        return Chatroom.builder()
                .sender(sender)
                .receiver(receiver)
                .build();
    }
}
