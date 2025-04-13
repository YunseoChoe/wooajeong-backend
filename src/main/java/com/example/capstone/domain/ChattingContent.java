package com.example.capstone.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chatting_content") // 실제 몽고 DB 컬렉션 이름
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChattingContent {
    @Id
    private String id; // 또는 ObjectId
    private String name;
    private Long age;

    public ChattingContent(String name, Long age) {
        this.name = name;
        this.age = age;
    }
}