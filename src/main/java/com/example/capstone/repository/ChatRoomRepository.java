package com.example.capstone.repository;

import com.example.capstone.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByRoomLink(String roomLink);
    List<ChatRoom> findAllByCreatorIdOrOpponentId(Long userId1, Long userId2);
}
