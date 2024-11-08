package com.ceos20.instagram_clone.domain.chat.controller;

import com.ceos20.instagram_clone.domain.chat.dto.request.ChatroomRequestDto;
import com.ceos20.instagram_clone.domain.chat.dto.request.MessageRequestDto;
import com.ceos20.instagram_clone.domain.chat.dto.response.ChatroomResponseDto;
import com.ceos20.instagram_clone.domain.chat.dto.response.MessageResponseDto;
import com.ceos20.instagram_clone.domain.chat.service.ChatService;
import com.ceos20.instagram_clone.domain.member.dto.CustomUserDetails;
import com.ceos20.instagram_clone.global.common.response.CommonResponse;
import com.ceos20.instagram_clone.global.common.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Tag(name = "Chat", description = "디엠 관련 API")
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "채팅방 생성", description = "채팅방을 처음으로 생성하는 API")
    @PostMapping
    public CommonResponse<ChatroomResponseDto> createChatroom(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody ChatroomRequestDto request) {

        return new CommonResponse<>(ResponseCode.SUCCESS, chatService.createChatroom(request, userDetails.getMemberId()));
    }

    @Operation(summary = "메세지 전송", description = "메세지를 전송하는 API")
    @PostMapping("/{chatroomId}/message")
    public CommonResponse<MessageResponseDto> createMessage(@PathVariable Long chatroomId, @AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody MessageRequestDto request) {

        return new CommonResponse<>(ResponseCode.SUCCESS, chatService.createMessage(request, chatroomId, userDetails.getMemberId()));
    }

    @Operation(summary = "메세지 전체 조회", description = "특정 채팅방 내 전체 메세지를 조회하는 API")
    @GetMapping("/{chatroomId}/message")
    public CommonResponse<List<MessageResponseDto>> getAllMessagesInChatroom(@PathVariable Long chatroomId, @AuthenticationPrincipal CustomUserDetails userDetails) {

        return new CommonResponse<>(ResponseCode.SUCCESS, chatService.getMessageInChatroom(chatroomId, userDetails.getMemberId()));
    }

    @Operation(summary = "1:1 채팅방 조회", description = "1:1 채팅방을 조회하는 API")
    @GetMapping("/{senderId}/{receiverId}")
    public CommonResponse<ChatroomResponseDto> getChatroom(@PathVariable Long senderId, @PathVariable Long receiverId) {

        return new CommonResponse<>(ResponseCode.SUCCESS, chatService.getChatroom(senderId, receiverId));
    }

    @Operation(summary = "전체 채팅방 조회", description = "사용자가 속해있는 전체 채팅방을 조회하는 API")
    @GetMapping
    public CommonResponse<List<ChatroomResponseDto>> getMyChatroomList(@AuthenticationPrincipal CustomUserDetails userDetails) {

        return new CommonResponse<>(ResponseCode.SUCCESS, chatService.getMyChatroomList(userDetails.getMemberId()));
    }

}
