package com.ceos20.instagram_clone.domain.chat.service;

import com.ceos20.instagram_clone.domain.chat.dto.request.ChatroomReq;
import com.ceos20.instagram_clone.domain.chat.dto.request.MessageReq;
import com.ceos20.instagram_clone.domain.chat.dto.response.ChatroomRes;
import com.ceos20.instagram_clone.domain.chat.dto.response.MessageRes;
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
        return memberRepository.findById(memberId).orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));
    }

    public Chatroom findChatroomById(Long chatroomId) {
        return chatroomRepository.findById(chatroomId).orElseThrow(() -> new BadRequestException(NOT_FOUND_CHATROOM_ID));
    }

    /**
     * 채팅방 생성
     * **/
    @Transactional
    public ChatroomRes createChatroom(ChatroomReq request) {

        Member sender = findMemberById(request.senderId());
        Member receiver = findMemberById(request.receiverId());

        Optional<Chatroom> existChatroom = chatroomRepository.findBySenderAndReceiver(sender, receiver);
        if (existChatroom.isPresent()) {
            throw new BadRequestException(VALID_CHATROOM);
        }

        Chatroom chatroom = chatroomRepository.save(request.toEntity(sender, receiver));
        return ChatroomRes.of(chatroom);
    }

    /**
     * 메세지 전송
     * **/
    @Transactional
    public MessageRes createMessage(MessageReq request) {

        Member sender = findMemberById(request.senderId());
        Chatroom chatroom = findChatroomById(request.chatroomId());

        Message message = messageRepository.save(request.toEntity(sender, chatroom));
        return MessageRes.of(message);
    }

    /**
     * 채팅방 내 디엠 조회
     * **/
    public List<MessageRes> getMessageInChatroom(Long chatroomId) {

        Chatroom chatroom = findChatroomById(chatroomId);
        List<Message> messages = messageRepository.findAllByChatroom(chatroom);

        return messages.stream()
                .map(MessageRes::of)
                .collect(Collectors.toList());
    }

    /**
     * 1:1 채팅방 조회
     * **/
    public ChatroomRes getChatroom(Long senderId, Long receiverId) {

        Member sender = findMemberById(senderId);
        Member receiver = findMemberById(receiverId);

        Chatroom chatroom = chatroomRepository.findBySenderAndReceiver(sender, receiver)
                .orElseThrow(() -> new BadRequestException(INVALID_CHATROOM));

        return ChatroomRes.of(chatroom);
    }

    /**
     * 내가 참여한 모든 채팅방 리스트 조회
     * **/
    public List<ChatroomRes> getMyChatroomList(Long memberId) {

        Member member = findMemberById(memberId);

        List<Chatroom> chatrooms = chatroomRepository.findAllBySenderOrReceiver(member, member);

        return chatrooms.stream()
                .map(ChatroomRes::of)
                .collect(Collectors.toList());
    }
}
