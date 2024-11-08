package com.ceos20.instagram_clone.domain.comment.controller;

import com.ceos20.instagram_clone.domain.comment.dto.request.CommentRequestDto;
import com.ceos20.instagram_clone.domain.comment.entity.Comment;
import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.domain.post.entity.Post;
import com.ceos20.instagram_clone.global.repository.CommentRepository;
import com.ceos20.instagram_clone.global.repository.MemberRepository;
import com.ceos20.instagram_clone.global.repository.PostRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    private Long memberId;
    private Long postId;
    private Long commentId;

    @BeforeEach
    public void setUp() {

        // Member 생성 및 저장
        Member member = new Member("Ceos", "ceos@naver", "1234", "ceos-backend", "http://link.url", "안녕", null, "", null);
        memberRepository.save(member);
        memberId = member.getId();

        // Post 생성 및 저장
        Post post = new Post("테스트 게시글입니다", "서울시", "음악", member);
        postRepository.save(post);
        postId = post.getId();

        // Comment 생성 및 저장
        Comment comment1 = new Comment("테스트 댓글입니다", post, member, null);
        Comment comment2 = new Comment("테스트 댓글2입니다", post, member, null);
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentId = comment1.getId();
    }

    @Test
    public void 댓글_작성_성공() throws Exception {

        // given
        CommentRequestDto request = new CommentRequestDto("첫 댓글입니다", null);

        // when & then
        mockMvc.perform(post("/api/post/{postId}/comment/{memberId}", postId, memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.content").value("첫 댓글입니다"));
    }

    @Test
    public void 대댓글_작성_성공() throws Exception {

        // given
        CommentRequestDto request = new CommentRequestDto("대댓글입니다", commentId);

        // when & then
        mockMvc.perform(post("/api/post/{postId}/comment/{memberId}", postId, memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.content").value("대댓글입니다"))
                .andExpect(jsonPath("$.result.parentCommentId").value(commentId));
    }

    @Test
    public void 게시물_전체_댓글_조회_성공() throws Exception {

        // when & then
        mockMvc.perform(get("/api/post/{postId}/comment", postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0].content").value("테스트 댓글입니다"));
    }

}