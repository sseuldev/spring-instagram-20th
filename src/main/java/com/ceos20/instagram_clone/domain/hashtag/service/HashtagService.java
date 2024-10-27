package com.ceos20.instagram_clone.domain.hashtag.service;

import com.ceos20.instagram_clone.domain.hashtag.dto.request.HashtagRequestDto;
import com.ceos20.instagram_clone.domain.hashtag.dto.request.PosthashtagRequestDto;
import com.ceos20.instagram_clone.domain.hashtag.dto.response.HashtagResponseDto;
import com.ceos20.instagram_clone.domain.hashtag.dto.response.PosthashtagResponseDto;
import com.ceos20.instagram_clone.domain.hashtag.entity.Hashtag;
import com.ceos20.instagram_clone.domain.hashtag.entity.Posthashtag;
import com.ceos20.instagram_clone.domain.member.entity.Member;
import com.ceos20.instagram_clone.domain.post.entity.Post;
import com.ceos20.instagram_clone.global.exception.BadRequestException;
import com.ceos20.instagram_clone.global.repository.HashtagRepository;
import com.ceos20.instagram_clone.global.repository.MemberRepository;
import com.ceos20.instagram_clone.global.repository.PostRepository;
import com.ceos20.instagram_clone.global.repository.PosthashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.ceos20.instagram_clone.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HashtagService {

    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;
    private final PosthashtagRepository posthashtagRepository;

    public Post findPostById(Long postId) {
        return postRepository.findByIdAndDeletedAtIsNull(postId).orElseThrow(() -> new BadRequestException(NOT_FOUND_POST_ID));
    }

    @Transactional
    public PosthashtagResponseDto addHashtagsToPost(Long postId, List<String> hashtags) {

        Post post = findPostById(postId);

        List<Hashtag> postHashtags = hashtags.stream()
                .map(this::findOrCreateHashtag)
                .toList();

        savePostHashtags(post, postHashtags);

        return PosthashtagResponseDto.from(post, postHashtags);
    }

    private void savePostHashtags(Post post, List<Hashtag> postHashtags) {
        postHashtags.forEach(hashtag -> {
            Posthashtag posthashtag = Posthashtag.builder()
                    .post(post)
                    .hashtag(hashtag)
                    .build();
            posthashtagRepository.save(posthashtag);
        });
    }

    private Hashtag findOrCreateHashtag(String name) {
        return hashtagRepository.findByName(name)
                .map(this::increaseHashtagCount)
                .orElseGet(() -> createHashtag(new HashtagRequestDto(name)));
    }

    private Hashtag increaseHashtagCount(Hashtag hashtag) {
        hashtag.increaseCount();
        return hashtagRepository.save(hashtag);
    }

    @Transactional
    public Hashtag createHashtag(HashtagRequestDto request) {
        if (hashtagRepository.findByName(request.name()).isPresent()) {
            throw new BadRequestException(VALID_HASHTAG);
        }
        Hashtag newHashtag = hashtagRepository.save(request.toEntity());
        newHashtag.increaseCount();
        return newHashtag;
    }

}
