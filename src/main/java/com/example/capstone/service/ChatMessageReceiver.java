//package com.example.capstone.service;
//
//import com.example.capstone.dto.ChatMessageDto;
//import com.example.capstone.handler.ChatWebSocketHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//
//// RabbitMQ에서 메세지 수신 (Consumer역할: 특정 Queue를 구독하고, 받은 메세지를 Websocket으로 유저에게 전달)
//@Service
//public class ChatMessageReceiver {
//
//    private final ChatWebSocketHandler handler;
//
//    public ChatMessageReceiver(ChatWebSocketHandler handler) {
//        this.handler = handler;
//    }
//
//    // Queue 구독
//    @RabbitListener(queues = "chat.queue.user1") // 예시: userB만 처리
//    public void receive(ChatMessageDto message) throws IOException {
//        handler.sendToUser(Integer.parseInt(message.getReceiverId()), message);
//    }
//}
