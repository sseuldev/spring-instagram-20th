package com.ceos20.instagram_clone.global.repository;

import com.ceos20.instagram_clone.domain.chat.entity.Chatroom;
import com.ceos20.instagram_clone.domain.comment.entity.Comment;
import com.ceos20.instagram_clone.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
    Optional<Chatroom> findBySenderAndReceiverAndDeletedAtIsNull(Member sender, Member receiver);

    List<Chatroom> findAllBySenderOrReceiverAndDeletedAtIsNull(Member sender, Member receiver);

    Optional<Chatroom> findByIdAndDeletedAtIsNull(Long chatroomId);
}
