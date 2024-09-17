package com.ceos20.instagram_clone.domain;

import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.domain.post.entity.Post;
import com.ceos20.instagram_clone.global.repository.MemberRepository;
import com.ceos20.instagram_clone.global.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class PostRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;

    @Test
    public void 게시물_등록_테스트() throws Exception {

        //given
        Member member = Member.builder()
                .name("이한슬")
                .email("ceos@naver.com")
                .password("1234")
                .nickname("sseuldev")
                .build();

        Member newMember = memberRepository.save(member);

        Post post1 = Post.builder()
                .content("테스트1")
                .member(newMember)
                .build();

        Post post2 = Post.builder()
                .content("테스트2")
                .member(newMember)
                .build();

        Post post3 = Post.builder()
                .content("테스트3")
                .member(newMember)
                .build();

        // when
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        //then
        List<Post> posts = postRepository.findAllByMember(newMember);

        assertEquals(3, posts.size(), "게시물 개수는 총 3개입니다.");

        assertTrue(posts.stream().anyMatch(post -> post.getContent().equals("테스트1")));
        assertTrue(posts.stream().anyMatch(post -> post.getContent().equals("테스트2")));
        assertTrue(posts.stream().anyMatch(post -> post.getContent().equals("테스트3")));

        posts.forEach(post -> assertEquals(newMember.getId(), post.getMember().getId()));
    }
}
