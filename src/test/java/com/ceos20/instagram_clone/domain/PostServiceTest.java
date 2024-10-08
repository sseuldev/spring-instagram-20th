package com.ceos20.instagram_clone.domain;

import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.domain.post.dto.request.PostReq;
import com.ceos20.instagram_clone.domain.post.dto.response.PostRes;
import com.ceos20.instagram_clone.domain.post.entity.Image;
import com.ceos20.instagram_clone.domain.post.entity.Post;
import com.ceos20.instagram_clone.domain.post.service.PostService;
import com.ceos20.instagram_clone.global.repository.ImageRepository;
import com.ceos20.instagram_clone.global.repository.MemberRepository;
import com.ceos20.instagram_clone.global.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private PostService postService;

    private Member member;
    private Post post;
    private PostReq postReq;

    @BeforeEach
    void setUp() {

        member = Member.builder()
                .id(1L)
                .name("Ceos")
                .email("ceos@google.com")
                .nickname("Backend-ceos")
                .build();


        post = Post.builder()
                .id(1L)
                .content("Hello!")
                .member(member)
                .build();


        postReq = new PostReq(
                "Nice to meet you!",
                0,
                "Test Location",
                "Test Music",
                List.of("image1.jpg", "image2.jpg")
        );
    }

    @Test
    void 게시물_생성_테스트() {
        // given
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // when
        PostRes result = postService.createPost(postReq, 1L);

        // then
        assertNotNull(result);
        assertEquals(post.getId(), result.postId());
        verify(postRepository, times(1)).save(any(Post.class));
        verify(imageRepository, times(1)).saveAll(any(List.class));
    }

    @Test
    void 게시물_조회_테스트() {
        // given
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // when
        PostRes result = postService.getPost(1L);

        // then
        assertNotNull(result);
        assertEquals(post.getId(), result.postId());
        assertEquals(post.getContent(), result.content());
        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    void 게시물_삭제_테스트() {
        // given
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // when
        postService.deletePost(1L);

        // then
        verify(postRepository, times(1)).delete(post);
    }
}
