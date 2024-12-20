package com.ceos20.instagram_clone.domain.chat.service;

import com.ceos20.instagram_clone.domain.chat.dto.request.ChatroomRequestDto;
import com.ceos20.instagram_clone.domain.chat.dto.request.MessageRequestDto;
import com.ceos20.instagram_clone.domain.chat.dto.response.ChatroomResponseDto;
import com.ceos20.instagram_clone.domain.chat.dto.response.MessageResponseDto;
import com.ceos20.instagram_clone.domain.chat.entity.Chatroom;
import com.ceos20.instagram_clone.domain.chat.entity.Message;
import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.global.exception.BadRequestException;
import com.ceos20.instagram_clone.global.repository.ChatroomRepository;
import com.ceos20.instagram_clone.global.repository.MemberRepository;
import com.ceos20.instagram_clone.global.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ceos20.instagram_clone.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatroomRepository chatroomRepository;
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    public Member findMemberById(Long memberId) {
        return memberRepository.findByIdAndDeletedAtIsNull(memberId).orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));
    }

    public Chatroom findChatroomById(Long chatroomId) {
        return chatroomRepository.findByIdAndDeletedAtIsNull(chatroomId).orElseThrow(() -> new BadRequestException(NOT_FOUND_CHATROOM_ID));
    }

    /**
     * 채팅방 생성
     * **/
    @Transactional
    public ChatroomResponseDto createChatroom(ChatroomRequestDto request, Long senderId) {

        Member sender = findMemberById(senderId);
        Member receiver = findMemberById(request.receiverId());

        Optional<Chatroom> existChatroom = chatroomRepository.findBySenderAndReceiverAndDeletedAtIsNull(sender, receiver);
        if (existChatroom.isPresent()) {
            throw new BadRequestException(VALID_CHATROOM);
        }

        Chatroom chatroom = chatroomRepository.save(request.toEntity(sender, receiver));

        MessageRequestDto message = new MessageRequestDto(request.content());
        MessageResponseDto firstMessage = createMessage(message, chatroom.getId(), senderId);

        return ChatroomResponseDto.from(chatroom, firstMessage);
    }

    /**
     * 메세지 전송
     * **/
    @Transactional
    public MessageResponseDto createMessage(MessageRequestDto request, Long chatroomId, Long senderId) {

        Member sender = findMemberById(senderId);
        Chatroom chatroom = findChatroomById(chatroomId);

        Message message = messageRepository.save(request.toEntity(sender, chatroom));
        return MessageResponseDto.from(message);
    }

    /**
     * 채팅방 내 디엠 조회
     * **/
    public List<MessageResponseDto> getMessageInChatroom(Long chatroomId, Long memberId) {

        Chatroom chatroom = findChatroomById(chatroomId);
        Member member = findMemberById(memberId);

        if (!chatroom.getSender().equals(member) && !chatroom.getReceiver().equals(member)) {
            throw new BadRequestException(INVALID_CHATROOM_AUTHORITY);
        }

        List<Message> messages = messageRepository.findAllByChatroomAndDeletedAtIsNullOrderBySendTimeAsc(chatroom);

        return messages.stream()
                .map(MessageResponseDto::from)
                .toList();
    }

    /**
     * 1:1 채팅방 조회
     * **/
    public ChatroomResponseDto getChatroom(Long senderId, Long receiverId) {

        Member sender = findMemberById(senderId);
        Member receiver = findMemberById(receiverId);

        Chatroom chatroom = chatroomRepository.findChatroomByMembers(sender, receiver)
                .orElseThrow(() -> new BadRequestException(INVALID_CHATROOM));

        return ChatroomResponseDto.from(chatroom);
    }

    /**
     * 내가 참여한 모든 채팅방 리스트 조회
     * **/
    public List<ChatroomResponseDto> getMyChatroomList(Long memberId) {

        Member member = findMemberById(memberId);

        List<Chatroom> chatrooms = chatroomRepository.findAllBySenderOrReceiverAndDeletedAtIsNull(member, member);

        return chatrooms.stream()
                .map(ChatroomResponseDto::from)
                .toList();
    }
}
