package com.ceos20.instagram_clone.domain.post.service;

import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.domain.post.entity.Post;
import com.ceos20.instagram_clone.domain.post.entity.Postlike;
import com.ceos20.instagram_clone.global.exception.BadRequestException;
import com.ceos20.instagram_clone.global.repository.MemberRepository;
import com.ceos20.instagram_clone.global.repository.PostRepository;
import com.ceos20.instagram_clone.global.repository.PostlikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ceos20.instagram_clone.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostlikeService {

    private final PostlikeRepository postlikeRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public Member findMemberById(Long memberId) {
        return memberRepository.findByIdAndDeletedAtIsNull(memberId).orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));
    }

    public Post findPostById(Long postId) {
        return postRepository.findByIdAndDeletedAtIsNull(postId).orElseThrow(() -> new BadRequestException(NOT_FOUND_POST_ID));
    }

    /** [주요 기능 ]
     * 게시물 좋아요
     * **/
    @Transactional
    public void likePost(Long postId, Long memberId) {

        Post post = findPostById(postId);
        Member member = findMemberById(memberId);

        Postlike postlike = postlikeRepository.findPostlikeByMemberIdAndPostId(member.getId(), post.getId()).orElse(null);

        if(postlike == null) {
            postlikeRepository.save(Postlike.builder()
                    .member(member)
                    .post(post)
                    .build());
        }
    }

    /** [주요 기능 ]
     * 게시물 좋아요 삭제
     * **/
    @Transactional
    public void cancelPostlike(Long postId, Long memberId) {

        Postlike postlike = postlikeRepository.findPostlikeByMemberIdAndPostId(memberId, postId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_POST_LIKE));

        postlikeRepository.delete(postlike);
    }
}
