package com.ceos20.instagram_clone.domain.post.repository;

import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.domain.post.entity.Image;
import com.ceos20.instagram_clone.domain.post.entity.Post;
import com.ceos20.instagram_clone.global.repository.ImageRepository;
import com.ceos20.instagram_clone.global.repository.MemberRepository;
import com.ceos20.instagram_clone.global.repository.PostRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class PostRepositoryTest {

    private final PostRepository postRepository;
    private final EntityManager em;

    @Autowired
    public PostRepositoryTest(PostRepository postRepository, EntityManager em) {
        this.postRepository = postRepository;
        this.em = em;
    }

    Member member;

    Post post1;
    Post post2;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .name("Ceos")
                .email("ceos@google.com")
                .password("1234")
                .nickname("backend-ceos")
                .build();
        em.persist(member);

        post1 = Post.builder()
                .content("Hello!!")
                .member(member)
                .build();
        em.persist(post1);

        post2 = Post.builder()
                .content("Hi!!")
                .member(member)
                .build();
        em.persist(post2);

        Image image1 = Image.builder()
                .imageUrl("imageUrl1.jpg")
                .post(post1)
                .build();
        Image image2 = Image.builder()
                .imageUrl("imageUrl2.jpg")
                .post(post1)
                .build();
        Image image3 = Image.builder()
                .imageUrl("imageUrl3.jpg")
                .post(post2)
                .build();

        post1.getImages().add(image1);
        post1.getImages().add(image2);
        post2.getImages().add(image3);

        em.persist(image1);
        em.persist(image2);
        em.persist(image3);

        em.flush();
        em.clear();
    }

    // 1번의 Post 조회 쿼리 후, 각 Post에 연결된 N개의 Image를 조회하기 위해 추가 쿼리들이 실행
    @Test
    @Transactional
    public void NPlusOne_확인_테스트() {

        List<Post> posts = postRepository.findAll();

        for (Post post : posts) {
            System.out.println("Post content: " + post.getContent());

            // N + 1 발생 구간
            List<Image> images = post.getImages();

            for (Image image : images) {
                System.out.println("Image URL: " + image.getImageUrl());
            }
        }
    }

    // left join 을 사용하여 image 테이블을 조인
    // 게시글에 속한 이미지를 함께 조회
    @Test
    @Transactional
    public void FetchJoin_테스트() {

        Post post = postRepository.findByIdWithImages(post1.getId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        List<Image> images = post.getImages();

        assertEquals(2, images.size());
    }
}