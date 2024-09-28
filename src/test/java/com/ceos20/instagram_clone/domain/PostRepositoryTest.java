package com.ceos20.instagram_clone.domain;

import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.domain.post.entity.Image;
import com.ceos20.instagram_clone.domain.post.entity.Post;
import com.ceos20.instagram_clone.global.repository.ImageRepository;
import com.ceos20.instagram_clone.global.repository.MemberRepository;
import com.ceos20.instagram_clone.global.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;
    private Post post;

    @BeforeEach
    void setUp() {

        member = Member.builder()
                .name("Ceos")
                .email("ceos@google.com")
                .password("1234")
                .nickname("backend-ceos")
                .build();
        memberRepository.save(member);

        post = Post.builder()
                .content("Hello!!")
                .member(member)
                .build();
        postRepository.save(post);

        Image image1 = Image.builder()
                .imageUrl("imageUrl1.jpg")
                .post(post)
                .build();
        Image image2 = Image.builder()
                .imageUrl("imageUrl2.jpg")
                .post(post)
                .build();

        post.getImages().add(image1);
        post.getImages().add(image2);
        postRepository.save(post);
    }

    @Test
    @Transactional
    public void NPlusOne_확인_테스트() {

        List<Post> posts = postRepository.findAll();


        for (Post retrievedPost : posts) {
            System.out.println("Post content: " + retrievedPost.getContent());

            List<Image> images = retrievedPost.getImages();
            for (Image image : images) {
                System.out.println("Image URL: " + image.getImageUrl());
            }
        }
    }

    @Test
    @Transactional
    public void FetchJoin_테스트() {

        Post retrievedPost = postRepository.findByIdWithImages(post.getId())
                .orElseThrow(() -> new RuntimeException("Post not found"));


        List<Image> images = retrievedPost.getImages();
        assertEquals(2, images.size());
    }
}