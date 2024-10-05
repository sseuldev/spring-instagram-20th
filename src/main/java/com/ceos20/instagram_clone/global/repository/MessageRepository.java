package com.ceos20.instagram_clone.global.repository;

import com.ceos20.instagram_clone.domain.chat.entity.Chatroom;
import com.ceos20.instagram_clone.domain.chat.entity.Message;
import com.ceos20.instagram_clone.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByChatroomAndDeletedAtIsNull(Chatroom chatroom);
}
