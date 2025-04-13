package com.example.capstone.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ChatMessageDto implements Serializable {
    private String roomId;
    private String senderId;
    private String receiverId;
    private String content;
    private String sendTime;
    private Boolean isRead;
}
