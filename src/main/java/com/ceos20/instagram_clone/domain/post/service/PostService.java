package com.ceos20.instagram_clone.domain.post.service;

import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.domain.post.dto.request.PostReq;
import com.ceos20.instagram_clone.domain.post.dto.response.PostRes;
import com.ceos20.instagram_clone.domain.post.entity.Image;
import com.ceos20.instagram_clone.domain.post.entity.Post;
import com.ceos20.instagram_clone.global.exception.BadRequestException;
import com.ceos20.instagram_clone.global.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.ceos20.instagram_clone.global.exception.ExceptionCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;

    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));
    }

    public Post findPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new BadRequestException(NOT_FOUND_POST_ID));
    }

    /** [ 주요기능 ]
     * 게시물 생성 (게시글에 사진과 함께 글 작성하기)
     * **/
    @Transactional
    public PostRes createPost(PostReq request, Long memberId) {

        Member member = findMemberById(memberId);

        Post post = request.from(member);
        Post savePost = postRepository.save(post);

        List<Image> images = convertToImageEntities(request.imageUrls(), savePost);
        images.forEach(imageRepository::save);

        return PostRes.createPostRes(savePost);
    }

    /* 이미지 URL 리스트를 Image 엔티티로 변환 */
    private List<Image> convertToImageEntities(List<String> imageUrls, Post post) {

        if (imageUrls == null || imageUrls.isEmpty()) {
            throw new BadRequestException(EMPTY_IMAGE);
        }

        return imageUrls.stream()
                .map(imageUrl -> Image.builder()
                        .imageUrl(imageUrl)
                        .post(post)
                        .build())
                .collect(Collectors.toList());
    }

    /** [ 주요기능 ]
     * 게시물 조회
     * **/
    public PostRes getPost(Long postId) {

        Post post = findPostById(postId);

        return PostRes.getPostRes(post);
    }

    /** [ 주요기능 ]
     * 게시물 삭제
     * **/
    public void deletePost(Long postId) {
        
        Post post = findPostById(postId);
        
        postRepository.delete(post);
    }
}
