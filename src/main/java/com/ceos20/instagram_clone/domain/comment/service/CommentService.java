package com.ceos20.instagram_clone.domain.comment.service;

import com.ceos20.instagram_clone.domain.comment.dto.request.CommentRequestDto;
import com.ceos20.instagram_clone.domain.comment.dto.response.CommentResponseDto;
import com.ceos20.instagram_clone.domain.comment.entity.Comment;
import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.domain.post.entity.Post;
import com.ceos20.instagram_clone.global.exception.BadRequestException;
import com.ceos20.instagram_clone.global.repository.CommentRepository;
import com.ceos20.instagram_clone.global.repository.MemberRepository;
import com.ceos20.instagram_clone.global.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ceos20.instagram_clone.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));
    }

    public Post findPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new BadRequestException(NOT_FOUND_POST_ID));
    }

    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new BadRequestException(NOT_FOUND_COMMENT_ID));
    }

    /** [주요 기능]
     * 댓글 생성
     * **/
    @Transactional
    public CommentResponseDto createComment(CommentRequestDto request) {

        Post post = findPostById(request.postId());
        Member member = findMemberById(request.memberId());

        Comment parentComment = null;
        if (request.parentCommentId() != null) {
            parentComment = findCommentById(request.parentCommentId());
        }

        Comment comment = request.toEntity(post, member, parentComment);
        Comment saveComment = commentRepository.save(comment);

        post.updateCommentCount(post.getCommentCount() + 1);
        postRepository.save(post);

        return CommentResponseDto.from(saveComment);
    }

    /** [주요 기능 ]
     * 댓글 조회
     * **/
    public CommentResponseDto getComment(Long commentId) {

        Comment comment = findCommentById(commentId);

        return CommentResponseDto.from(comment);
    }

    /** [주요 기능 ]
     * 댓글 삭제
     * **/
    @Transactional
    public void deleteComment(Long commentId) {

        Comment comment = findCommentById(commentId);
        Post post = comment.getPost();

        commentRepository.delete(comment);

        post.updateCommentCount(post.getCommentCount() - 1);
        postRepository.save(post);
    }

}
