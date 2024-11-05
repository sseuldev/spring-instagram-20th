package com.ceos20.instagram_clone.domain.chat.controller;

import com.ceos20.instagram_clone.domain.chat.dto.request.ChatroomRequestDto;
import com.ceos20.instagram_clone.domain.chat.dto.request.MessageRequestDto;
import com.ceos20.instagram_clone.domain.chat.entity.Chatroom;
import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.global.repository.ChatroomRepository;
import com.ceos20.instagram_clone.global.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChatroomRepository chatroomRepository;

    private Long senderId;
    private Long receiverId;
    private Long chatroomId;

    @BeforeEach
    public void setUp() {

        // Sender 및 Receiver 멤버 생성 및 저장
        Member sender = new Member("보내는애", "sender@naver", null, "0000", "보내는사람이야", "http://link.url", "", null, "", null);
        Member receiver1 = new Member("받는애", "receiver@naver", null, "1234", "받는사람이야", "http://link.url", "", null, "", null);
        Member receiver2 = new Member("받는애2", "receiver2@naver", null, "1234", "받는사람이야", "http://link.url", "", null, "", null);
        memberRepository.save(sender);
        memberRepository.save(receiver1);
        memberRepository.save(receiver2);
        senderId = sender.getId();
        receiverId = receiver1.getId();

        // Chatroom 생성 및 저장
        Chatroom chatroom1 = new Chatroom(sender, receiver1);
        Chatroom chatroom2 = new Chatroom(sender, receiver2);
        chatroomRepository.save(chatroom1);
        chatroomRepository.save(chatroom2);
        chatroomId = chatroom1.getId();
    }

    @Test
    public void 채팅방_생성_성공() throws Exception {

        // given
        ChatroomRequestDto request = new ChatroomRequestDto(senderId, "첫 메세지");

        // when & then
        mockMvc.perform(post("/api/chat/{memberId}", senderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.senderId").value(senderId));
    }

    @Test
    public void 메세지_생성_성공() throws Exception {

        // given
        MessageRequestDto request = new MessageRequestDto("잘지내니?");

        // when & then
        mockMvc.perform(post("/api/chat/{chatroomId}/message/{memberId}", chatroomId, senderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.content").value("잘지내니?"));
    }

    @Test
    public void 채팅방_조회_성공() throws Exception {

        // when & then
        mockMvc.perform(get("/api/chat/{senderId}/{receiverId}", senderId, receiverId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.senderId").value(senderId))
                .andExpect(jsonPath("$.result.receiverId").value(receiverId));
    }

    @Test
    public void 채팅방_리스트_조회_성공() throws Exception {

        // when & then
        mockMvc.perform(get("/api/chat/{memberId}", senderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray());
    }

}