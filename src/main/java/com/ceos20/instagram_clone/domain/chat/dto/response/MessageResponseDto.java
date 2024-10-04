package com.ceos20.instagram_clone.domain.chat.dto.response;

import com.ceos20.instagram_clone.domain.chat.entity.Message;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MessageResponseDto(
        Long messageId,
        String content,
        LocalDateTime sendTime,
        Long senderId,
        Long chatroomId
) {
    public static MessageResponseDto of(Message message) {
        return MessageResponseDto.builder()
                .messageId(message.getId())
                .content(message.getContent())
                .sendTime(message.getSendTime())
                .senderId(message.getSender().getId())
                .chatroomId(message.getChatroom().getId())
                .build();
    }
}
