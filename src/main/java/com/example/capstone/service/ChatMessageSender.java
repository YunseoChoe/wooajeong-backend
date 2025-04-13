//package com.example.capstone.service;
//
//import com.example.capstone.dto.ChatMessageDto;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.stereotype.Service;
//
//// Client로 부터 받은 메세지를 RabbitMQ로 전송 (Producer 역할)
//@Service
//public class ChatMessageSender {
//
//    private final RabbitTemplate rabbitTemplate;
//
//    public ChatMessageSender(RabbitTemplate rabbitTemplate) {
//        this.rabbitTemplate = rabbitTemplate;
//    }
//
//    public void send(ChatMessageDto message) {
//        String routingKey = "chat." + message.getReceiverId();
//        rabbitTemplate.convertAndSend("chat.exchange", routingKey, message);
//    }
//}
