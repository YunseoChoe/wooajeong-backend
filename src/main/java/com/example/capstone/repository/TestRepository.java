package com.example.capstone.repository;

import com.example.capstone.domain.ChattingContent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

// 채팅방 (roomId)을 기준으로 메세지 불러오기
public interface ChattingContentRepository extends MongoRepository<ChattingContent, String> {
    ChattingContent findChattingContentByRoomId(int roomId);
}
