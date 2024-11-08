package com.ceos20.instagram_clone.global.repository;

import com.ceos20.instagram_clone.domain.chat.entity.Chatroom;
import com.ceos20.instagram_clone.domain.comment.entity.Comment;
import com.ceos20.instagram_clone.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
    Optional<Chatroom> findBySenderAndReceiverAndDeletedAtIsNull(Member sender, Member receiver);

    List<Chatroom> findAllBySenderOrReceiverAndDeletedAtIsNull(Member sender, Member receiver);

//    Optional<Chatroom> findBySenderAndReceiverOrReceiverAndSender(Member sender1, Member receiver1, Member sender2, Member receiver2);

    @Query("SELECT c FROM Chatroom c WHERE " +
            "((c.sender = :sender AND c.receiver = :receiver) OR " +
            "(c.sender = :receiver AND c.receiver = :sender)) AND c.deletedAt IS NULL")
    Optional<Chatroom> findChatroomByMembers(@Param("sender") Member sender, @Param("receiver") Member receiver);

    Optional<Chatroom> findByIdAndDeletedAtIsNull(Long chatroomId);
}
