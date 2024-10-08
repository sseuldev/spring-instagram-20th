package com.ceos20.instagram_clone.domain.chat.dto.response;

import com.ceos20.instagram_clone.domain.chat.entity.Message;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MessageRes (
        Long messageId,
        String content,
        LocalDateTime sendTime,
        Long senderId,
        Long chatroomId
) {
    public static MessageRes of(Message message) {
        return MessageRes.builder()
                .messageId(message.getId())
                .content(message.getContent())
                .sendTime(message.getSendTime())
                .senderId(message.getSender().getId())
                .chatroomId(message.getChatroom().getId())
                .build();
    }
}
