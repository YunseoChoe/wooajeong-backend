package com.example.capstone.controller;

import com.example.capstone.domain.ChattingContent;
import com.example.capstone.dto.ChatMessageDto;
import com.example.capstone.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class StompChatController {

    private final RabbitTemplate rabbitTemplate;
    private final ChatMessageRepository chatMessageRepository;

    @MessageMapping("/chat.send")
    public void handleMessage(@Payload ChatMessageDto message) {
        // roomId는 클라이언트가 명시적으로 보내는 값

        // 시간 설정 (프론트에서 설정 안 한 경우)
        if (message.getSendTime() == null) {
            message.setSendTime(LocalDateTime.now());
        }

        // MongoDB에 저장
        ChattingContent entity = new ChattingContent();
        entity.setRoomId(message.getRoomId());
        entity.setSenderId(message.getSenderId());
        entity.setReceiverId(message.getReceiverId());
        entity.setContent(message.getContent());
        entity.setSendTime(message.getSendTime());
        entity.setIsRead(false);  // 기본값

        chatMessageRepository.save(entity);

        // 로그 출력
        System.out.printf("📩 메시지 수신 - roomId: %s, senderId: %d, content: %s%n",
                message.getRoomId(), message.getSenderId(), message.getContent());


        // RabbitMQ 발행 (방 기준 라우팅)
        String routingKey = "room." + message.getRoomId(); // 라우팅키는 room.roomId (방 단위 저장)
        rabbitTemplate.convertAndSend("chat.exchange", routingKey, message);
    }
}
