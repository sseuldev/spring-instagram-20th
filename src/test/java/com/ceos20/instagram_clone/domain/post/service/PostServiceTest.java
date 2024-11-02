package com.ceos20.instagram_clone.domain.post.service;

import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.domain.post.dto.request.PostRequestDto;
import com.ceos20.instagram_clone.domain.post.dto.response.PostResponseDto;
import com.ceos20.instagram_clone.domain.post.entity.Image;
import com.ceos20.instagram_clone.domain.post.entity.Post;
import com.ceos20.instagram_clone.domain.post.service.PostService;
import com.ceos20.instagram_clone.global.repository.ImageRepository;
import com.ceos20.instagram_clone.global.repository.MemberRepository;
import com.ceos20.instagram_clone.global.repository.PostRepository;
import org.assertj.core.api.Assertions;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private PostService postService;

    private Member member;
    private PostRequestDto postReq;
    private Post post;

    @BeforeEach
    void setUp() {

        member = Member.builder()
                .name("Ceos")
                .email("ceos@google.com")
                .nickname("Backend-ceos")
                .build();

        postReq = new PostRequestDto(
                "Hello!",
                0,
                "Test Location",
                "Test Music",
                List.of("image1.jpg", "image2.jpg")
        );
        post = postReq.toEntity(member);
    }

    @Test
    void 게시물_생성_테스트() {

        // given
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        given(postRepository.save(any(Post.class))).willReturn(post);

        // when
        PostResponseDto result = postService.createPost(postReq, 1L);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.content()).isEqualTo(postReq.content());
        Assertions.assertThat(result.location()).isEqualTo(postReq.location());
        Assertions.assertThat(result.music()).isEqualTo(postReq.music());

        List<Image> savedImages = post.getImages();
        Assertions.assertThat(savedImages).hasSize(2);
        Assertions.assertThat(savedImages.get(0).getImageUrl()).isEqualTo("image1.jpg");
        Assertions.assertThat(savedImages.get(1).getImageUrl()).isEqualTo("image2.jpg");

    }

    @Test
    void 게시물_조회_테스트() {

        // given
        Long postId = 1L;
        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        // when
        PostResponseDto result = postService.getPost(postId);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.content()).isEqualTo(postReq.content());
        Assertions.assertThat(result.location()).isEqualTo(postReq.location());
        Assertions.assertThat(result.music()).isEqualTo(postReq.music());

        List<String> imageUrls = result.imageUrls();
        Assertions.assertThat(imageUrls).hasSize(2);
        Assertions.assertThat(imageUrls).containsExactly("image1.jpg", "image2.jpg");
    }

    @Test
    void 게시물_삭제_테스트() {

        // given
        Long postId = 1L;
        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        // when
        postService.deletePost(postId);

        // then: 게시물 삭제에 대한 검증
        verify(postRepository, times(1)).findById(postId);  // 게시물 조회 검증
        verify(postRepository, times(1)).delete(post);  // 게시물 삭제 검증
    }
}
