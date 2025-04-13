package com.example.capstone.repository;

import com.example.capstone.domain.ChattingContent;
import org.springframework.data.mongodb.repository.MongoRepository;

// 채팅방 (roomId)을 기준으로 메세지 불러오기
public interface TestRepository extends MongoRepository<ChattingContent, String> {
    ChattingContent findChattingContentByName(String name);
}