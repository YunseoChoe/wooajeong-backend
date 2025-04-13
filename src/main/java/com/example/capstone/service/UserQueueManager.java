package com.example.capstone.service;

import org.springframework.amqp.core.*;
import org.springframework.stereotype.Service;

/**
 * userId를 기반으로 사용자별 Queue와 바인딩을 동적으로 생성하는 서비스.
 *
 * ex: userId = 1 → queue: chat.queue.1, routingKey: chat.1
 */
@Service
public class UserQueueManager {

    private final AmqpAdmin amqpAdmin;
    private final TopicExchange exchange;

    public UserQueueManager(AmqpAdmin amqpAdmin, TopicExchange exchange) {
        this.amqpAdmin = amqpAdmin;
        this.exchange = exchange;
    }

    public void declareUserQueue(int userId) {
        String queueName = "chat.queue." + userId;     // ex: chat.queue.1
        String routingKey = "chat." + userId;          // ex: chat.1

        Queue queue = new Queue(queueName, true);
        // Exchange와 Queue 바인딩 생성
        Binding binding = BindingBuilder.bind(queue)
                .to(exchange)
                .with(routingKey);

        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareBinding(binding);
    }
}
