package com.example.capstone.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatRoom {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    private Long roomId;

    @Column(unique = true, nullable = false)
    private String roomLink;

    @Column(nullable = false)
    private Long creatorId;   // 방을 만든 사람

    @Column(nullable = false)
    private Long opponentId;  // 상대방

    private LocalDateTime createdAt = LocalDateTime.now();
}
