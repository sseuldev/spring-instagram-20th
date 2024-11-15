package com.ceos20.instagram_clone.domain.post.service;

import com.ceos20.instagram_clone.domain.hashtag.entity.Hashtag;
import com.ceos20.instagram_clone.domain.hashtag.entity.Posthashtag;
import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.domain.post.dto.request.PostRequestDto;
import com.ceos20.instagram_clone.domain.post.dto.response.PostResponseDto;
import com.ceos20.instagram_clone.domain.post.entity.Image;
import com.ceos20.instagram_clone.domain.post.entity.Post;
import com.ceos20.instagram_clone.global.exception.BadRequestException;
import com.ceos20.instagram_clone.global.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ceos20.instagram_clone.global.exception.ExceptionCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostlikeRepository postlikeRepository;
    private final MemberRepository memberRepository;
    private final PosthashtagRepository posthashtagRepository;

    public Member findMemberById(Long memberId) {
        return memberRepository.findByIdAndDeletedAtIsNull(memberId).orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));
    }

    public Post findPostById(Long postId) {
        return postRepository.findByIdAndDeletedAtIsNull(postId).orElseThrow(() -> new BadRequestException(NOT_FOUND_POST_ID));
    }

    /** [ 주요기능 ]
     * 게시물 생성 (게시글에 사진과 함께 글 작성하기)
     * **/
    @Transactional
    public PostResponseDto createPost(PostRequestDto request, Long memberId) {

        Member member = findMemberById(memberId);

        Post post = request.toEntity(member);
        Post savePost = postRepository.save(post);

        return PostResponseDto.from(savePost);
    }

    /** [ 주요기능 ]
     * 게시물 조회
     * **/
    public PostResponseDto getPost(Long postId) {

        Post post = findPostById(postId);
        List<Posthashtag> postHashtags = posthashtagRepository.findByPostId(postId);

        List<Hashtag> hashtags = postHashtags.isEmpty() ? null :
                postHashtags.stream()
                        .map(Posthashtag::getHashtag)
                        .toList();

        return PostResponseDto.from(post, hashtags);
    }

    /** [ 주요기능 ]
     * 전체 게시물 조회
     * **/
    public List<PostResponseDto> getAllPosts(Long memberId) {

        Member member = findMemberById(memberId);
        List<Post> posts = postRepository.findAllByMemberAndDeletedAtIsNull(member);

        return posts.stream()
                .map(post -> {
                    List<Posthashtag> postHashtags = posthashtagRepository.findByPostId(post.getId());

                    List<Hashtag> hashtags = postHashtags.isEmpty() ? null :
                            postHashtags.stream()
                                    .map(Posthashtag::getHashtag)
                                    .toList();

                    return PostResponseDto.from(post, hashtags);
                })
                .toList();
    }

    /** [ 주요기능 ]
     * 게시물 삭제
     * **/
    @Transactional
    public void deletePost(Long postId) {
        
        Post post = findPostById(postId);
        postlikeRepository.deleteByPostId(postId);
        
        postRepository.delete(post);
    }
}
