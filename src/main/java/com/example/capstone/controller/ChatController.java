package com.example.capstone.controller;

import com.example.capstone.dto.ChatMessageDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final RabbitTemplate rabbitTemplate;

    public ChatController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/send")
    public void send(@RequestBody ChatMessageDto msg) {
        System.out.println("채팅 보내기.");
        String routingKey = "chat." + msg.getReceiverId();
        rabbitTemplate.convertAndSend("chat.exchange", routingKey, msg);
    }
}