package com.ceos20.instagram_clone.domain.chat.dto.response;

import com.ceos20.instagram_clone.domain.chat.entity.Chatroom;
import com.ceos20.instagram_clone.domain.chat.entity.Message;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatroomResponseDto(
        Long chatroomId,
        Long senderId,
        Long receiverId,
        LocalDateTime createdAt,
        MessageResponseDto firstMessage
) {
    public static ChatroomResponseDto from(Chatroom chatroom, MessageResponseDto message) {
        return ChatroomResponseDto.builder()
                .chatroomId(chatroom.getId())
                .senderId(chatroom.getSender().getId())
                .receiverId(chatroom.getReceiver().getId())
                .createdAt(chatroom.getCreatedAt())
                .firstMessage(message)
                .build();
    }

    public static ChatroomResponseDto from(Chatroom chatroom) {
        return ChatroomResponseDto.builder()
                .chatroomId(chatroom.getId())
                .senderId(chatroom.getSender().getId())
                .receiverId(chatroom.getReceiver().getId())
                .createdAt(chatroom.getCreatedAt())
                .build();
    }
}
