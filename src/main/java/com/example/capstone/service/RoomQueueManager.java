package com.example.capstone.service;

import org.springframework.amqp.core.*;
import org.springframework.stereotype.Service;

/**
 * roodId를 기반으로 채팅방 별 Queue와 바인딩을 동적으로 생성하는 서비스.
 *
 * ex: roomId = 1 → queue: chat.room-1, routingKey: room.1
 */
@Service
public class RoomQueueManager {

    private final AmqpAdmin amqpAdmin;
    private final TopicExchange chatExchange;

    public RoomQueueManager(AmqpAdmin amqpAdmin, TopicExchange chatExchange) {
        this.amqpAdmin = amqpAdmin;
        this.chatExchange = chatExchange;
    }

    public void declareRoomQueue(Long roomId) {
        String queueName = "chat.room-" + roomId;
        String routingKey = "room." + roomId;

        Queue queue = new Queue(queueName, true);
        Binding binding = BindingBuilder.bind(queue)
                .to(chatExchange)
                .with(routingKey);

        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareBinding(binding);
    }
}
