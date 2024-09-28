package com.ceos20.instagram_clone.domain.chat.dto.response;

import com.ceos20.instagram_clone.domain.chat.entity.Chatroom;
import lombok.Builder;

@Builder
public record ChatroomRes (
        Long chatroomId,
        Long senderId,
        Long receiverId
) {
    public static ChatroomRes of(Chatroom chatroom) {
        return ChatroomRes.builder()
                .chatroomId(chatroom.getId())
                .senderId(chatroom.getSender().getId())
                .receiverId(chatroom.getReceiver().getId())
                .build();
    }
}
