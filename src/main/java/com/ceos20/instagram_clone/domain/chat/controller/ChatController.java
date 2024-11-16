package com.ceos20.instagram_clone.domain.chat.controller;

import com.ceos20.instagram_clone.domain.chat.dto.request.ChatroomRequestDto;
import com.ceos20.instagram_clone.domain.chat.dto.request.MessageRequestDto;
import com.ceos20.instagram_clone.domain.chat.dto.response.ChatroomResponseDto;
import com.ceos20.instagram_clone.domain.chat.dto.response.MessageResponseDto;
import com.ceos20.instagram_clone.domain.chat.service.ChatService;
import com.ceos20.instagram_clone.global.common.response.CommonResponse;
import com.ceos20.instagram_clone.global.common.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Tag(name = "Chat", description = "디엠 관련 API")
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "채팅방 생성", description = "채팅방을 처음으로 생성하는 API")
    @PostMapping("/{memberId}")
    public CommonResponse<ChatroomResponseDto> createChatroom(@PathVariable Long memberId, @Valid @RequestBody ChatroomRequestDto request) {

        return new CommonResponse<>(ResponseCode.SUCCESS, chatService.createChatroom(request, memberId));
    }

    @Operation(summary = "메세지 전송", description = "메세지를 전송하는 API")
    @PostMapping("/{chatroomId}/message/{memberId}")
    public CommonResponse<MessageResponseDto> createMessage(@PathVariable Long chatroomId, @PathVariable Long memberId, @Valid @RequestBody MessageRequestDto request) {

        return new CommonResponse<>(ResponseCode.SUCCESS, chatService.createMessage(request, chatroomId, memberId));
    }

    @Operation(summary = "메세지 전체 조회", description = "특정 채팅방 내 전체 메세지를 조회하는 API")
    @GetMapping("/{chatroomId}/message/{memberId}")
    public CommonResponse<List<MessageResponseDto>> getAllMessagesInChatroom(@PathVariable Long chatroomId, @PathVariable Long memberId) {

        return new CommonResponse<>(ResponseCode.SUCCESS, chatService.getMessageInChatroom(chatroomId, memberId));
    }

    @Operation(summary = "1:1 채팅방 조회", description = "1:1 채팅방을 조회하는 API")
    @GetMapping("/{senderId}/{receiverId}")
    public CommonResponse<ChatroomResponseDto> getChatroom(@PathVariable Long senderId, @PathVariable Long receiverId) {

        return new CommonResponse<>(ResponseCode.SUCCESS, chatService.getChatroom(senderId, receiverId));
    }

    @Operation(summary = "전체 채팅방 조회", description = "사용자가 속해있는 전체 채팅방을 조회하는 API")
    @GetMapping("/{memberId}")
    public CommonResponse<List<ChatroomResponseDto>> getMyChatroomList(@PathVariable Long memberId) {

        return new CommonResponse<>(ResponseCode.SUCCESS, chatService.getMyChatroomList(memberId));
    }

}
