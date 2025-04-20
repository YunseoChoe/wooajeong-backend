package com.example.capstone.repository;

import com.example.capstone.domain.ChattingContent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChattingContent, String> {
    List<ChattingContent> findByRoomIdOrderBySendTimeAsc(String roomId); // room에 해당하는 모든 메세지를 오름차순 정렬 후 반환.
}
