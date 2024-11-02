package com.ceos20.instagram_clone.domain.post.controller;

import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.domain.post.dto.request.PostRequestDto;
import com.ceos20.instagram_clone.domain.post.entity.Post;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    private Long memberId;
    private Long postId;

    @BeforeEach
    public void setUp() {

        // member 생성 및 저장
        Member member = new Member("Ceos", "ceos@naver", "1234", "ceos-backend", "http://link.url", "안녕", null, "", null);
        memberRepository.save(member);
        memberId = member.getId();

        // post 생성 및 저장
        Post post1 = new Post("테스트 게시글1 입니다", "서울시", "music", member);
        Post post2 = new Post("테스트 게시글2 입니다", "부산시", "music", member);
        postRepository.save(post1);
        postRepository.save(post2);
        postId = post1.getId();
    }

    @Test
    public void 게시물_생성_성공() throws Exception {

        // given
        List<String> images = List.of("aaa", "bbb");
        PostRequestDto request = new PostRequestDto("새로운 게시글입니다",0, "서울시", "music", images);

        // when & then
        mockMvc.perform(post("/api/post/{memberId}", memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andDo(print()) // 요청과 응답 정보를 콘솔에 출력
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.content").value("새로운 게시글입니다"))
                .andExpect(jsonPath("$.result.location").value("서울시"))
                .andExpect(jsonPath("$.result.music").value("music"))
                .andExpect(jsonPath("$.result.imageUrls[0]").value("aaa"))
                .andExpect(jsonPath("$.result.imageUrls[1]").value("bbb"));
    }

    @Test
    public void 게시물_가져오기_성공() throws Exception {

        // when & then
        mockMvc.perform(get("/api/post/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists())
                .andExpect(jsonPath("$.result.content").value("테스트 게시글입니다"));
    }

    @Test
    public void 전체_게시물_가져오기_성공() throws Exception {

        // when & then
        mockMvc.perform(get("/api/post/my/{memberId}", memberId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0].content").value("테스트 게시글1 입니다"))
                .andExpect(jsonPath("$.result[1].content").value("테스트 게시글2 입니다"));
    }

    @Test
    public void 게시물_좋아요_성공() throws Exception {

        // when & then
        mockMvc.perform(post("/api/post/{postId}/{memberId}/like", postId, memberId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}