package com.example.capstone.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageDto implements Serializable {
    private String roomId;
    private int senderId;
    private int receiverId;
    private String content;
    private LocalDateTime sendTime;
    private Boolean isRead;
}
