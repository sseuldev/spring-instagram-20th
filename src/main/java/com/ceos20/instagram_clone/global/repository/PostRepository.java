package com.ceos20.instagram_clone.global.repository;

import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.domain.post.entity.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JpaRepository 없이 구현하기
 */
@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    public Post save(Post post) {
        em.persist(post);
        return post;
    }

    public List<Post> findAllByMember(Member member) {
        return em.createQuery("SELECT p FROM Post p WHERE p.member = :member", Post.class)
                .setParameter("member", member)
                .getResultList();
    }

    public Optional<Post> findById(Long id) {
        Post post = em.find(Post.class, id);
        return Optional.ofNullable(post);
    }

    public void deleteById(Long id) {
        Post post = em.find(Post.class, id);
        if (post != null) {
            em.remove(post);
        }
    }

}
