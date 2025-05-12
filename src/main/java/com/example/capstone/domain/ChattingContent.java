package com.example.capstone.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chatting_content") // 실제 몽고 DB 컬렉션 이름
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChattingContent {
    @Id
    private String id; // 각 메세지 고유의 id

    private long roomId;
    private int senderId;
    private int receiverId;
    private String content;
    private LocalDateTime sendTime;
    private Boolean isRead;
}
