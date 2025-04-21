package com.example.capstone.controller;

import com.example.capstone.domain.ChattingContent;
import com.example.capstone.dto.ChatMessageDto;
import com.example.capstone.repository.ChatMessageRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

            // roomId 강제 하드 코딩. (ex. 1 -> 2: room-1-2 / 2 -> 1: room-1-2)
            String fixedRoomId = generateRoomId(msg.getSenderId(), msg.getReceiverId());
            msg.setRoomId(fixedRoomId);

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

            System.out.println("저장 직전 roomId: " + entity.getRoomId());
            System.out.println("저장 직전 sendTime: " + entity.getSendTime());

            chatMessageRepository.save(entity);

            System.out.println("메세지 저장.");
            return ResponseEntity.ok("메시지 전송 및 저장 완료"); // Client로 보내는 메세지.

        }
        catch (Exception e) {
            e.printStackTrace();  // 서버 로그에 예외 출력
            return ResponseEntity
                    .status(500)
                    .body("서버 오류로 인해 메시지를 전송하지 못했습니다: " + e.getMessage());
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<?> getMessages(@RequestParam String roomId) {
        try {
            List<ChattingContent> messages = chatMessageRepository.findByRoomIdOrderBySendTimeAsc(roomId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(500)
                    .body("메시지 불러오기 실패: " + e.getMessage());
        }
    }

    private String generateRoomId(int userId1, int userId2) {
        int low = Math.min(userId1, userId2);
        int high = Math.max(userId1, userId2);
        return "room-" + low + "-" + high;
    }
}
