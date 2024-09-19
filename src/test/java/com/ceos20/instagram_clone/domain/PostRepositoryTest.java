package com.ceos20.instagram_clone.domain;

import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.domain.post.entity.Post;
import com.ceos20.instagram_clone.global.repository.MemberRepository;
import com.ceos20.instagram_clone.global.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;

    private Member newMember;
    private Post post1;
    private Post post2;
    private Post post3;

    @BeforeEach
    void 기본세팅() {

        // given
        Member member = Member.builder()
                .name("이한슬")
                .email("ceos@naver.com")
                .password("1234")
                .nickname("sseuldev")
                .build();
        newMember = memberRepository.save(member);

        post1 = Post.builder()
                .content("테스트1")
                .member(newMember)
                .build();
        post2 = Post.builder()
                .content("테스트2")
                .member(newMember)
                .build();
        post3 = Post.builder()
                .content("테스트3")
                .member(newMember)
                .build();

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
    }

    @Test
    public void 게시물_조회_테스트() throws Exception {

        // given & when
        List<Post> posts = postRepository.findAllByMember(newMember);

        // then
        assertEquals(3, posts.size(), "게시물 개수는 총 3개입니다.");

        assertTrue(posts.stream().anyMatch(post -> post.getContent().equals("테스트1")));
        assertTrue(posts.stream().anyMatch(post -> post.getContent().equals("테스트2")));
        assertTrue(posts.stream().anyMatch(post -> post.getContent().equals("테스트3")));

        posts.forEach(post -> assertEquals(newMember.getId(), post.getMember().getId()));
    }

    @Test
    public void 게시물_삭제_테스트() throws Exception {

        // given & when
        postRepository.deleteById(post1.getId());

        // then
        List<Post> posts = postRepository.findAllByMember(newMember);

        assertEquals(2, posts.size(), "게시물 개수는 총 2개입니다.");

        Optional<Post> deletedPost = postRepository.findById(post1.getId());
        assertFalse(deletedPost.isPresent(), "삭제된 게시물이므로 존재하면 안됩니다!");
    }
}
