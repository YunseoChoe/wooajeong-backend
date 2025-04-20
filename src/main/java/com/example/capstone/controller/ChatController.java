package com.example.capstone.controller;

import com.example.capstone.domain.ChattingContent;
import com.example.capstone.dto.ChatMessageDto;
import com.example.capstone.repository.ChatMessageRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final RabbitTemplate rabbitTemplate;
    private final ChatMessageRepository chatMessageRepository;

    public ChatController(RabbitTemplate rabbitTemplate, ChatMessageRepository chatMessageRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.chatMessageRepository = chatMessageRepository;
    }

    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestBody ChatMessageDto msg) {
        try {
            System.out.println("채팅 보내기.");

            // RabbitMQ로 전송
            String routingKey = "chat." + msg.getReceiverId();
            rabbitTemplate.convertAndSend("chat.exchange", routingKey, msg);

            // MongoDB에 저장
            ChattingContent entity = new ChattingContent();
            entity.setRoomId(msg.getRoomId());
            entity.setSenderId(msg.getSenderId());
            entity.setReceiverId(msg.getReceiverId());
            entity.setContent(msg.getContent());
            entity.setSendTime(msg.getSendTime()); // 또는 LocalDateTime.now()
            entity.setIsRead(msg.getIsRead());

            chatMessageRepository.save(entity);

            System.out.println("메세지 저장.");
            return ResponseEntity.ok("메시지 전송 및 저장 완료");

        }
        catch (Exception e) {
            e.printStackTrace();  // 서버 로그에 예외 출력
            return ResponseEntity
                    .status(500)
                    .body("서버 오류로 인해 메시지를 전송하지 못했습니다: " + e.getMessage());
        }
    }
}