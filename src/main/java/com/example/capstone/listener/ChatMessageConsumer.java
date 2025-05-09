package com.example.capstone.listener;

import com.example.capstone.dto.ChatMessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class ChatMessageConsumer {

    private final SimpMessagingTemplate messagingTemplate;
    private final ConnectionFactory connectionFactory; // RabbitMQ 연결 설정 객체
    private final Map<String, SimpleMessageListenerContainer> containers = new ConcurrentHashMap<>();

    /**
     * roomId를 기반으로 RabbitMQ 큐에 대한 리스너를 동적으로 생성하고 실행
     * @param roomId 채팅방 고유 ID
     */
    public void startListenerForRoom(Long roomId) {
        String queueName = "chat.room-" + roomId;
        if (containers.containsKey(queueName)) return;

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);

        container.setMessageListener((Message message) -> {
            try {
                ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
                ChatMessageDto chatMessage = mapper.readValue(message.getBody(), ChatMessageDto.class);

                // STOMP 구독자에게 메세지 전송
                forwardMessage(chatMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        container.start();
        containers.put(queueName, container);
    }

    /**
     * STOMP를 통해 구독 중인 클라이언트에게 메시지를 전송
     * @param content MQ에서 받은 채팅 메시지
     */
    public void forwardMessage(ChatMessageDto content) {
        String destination = "/exchange/chat.exchange/room." + content.getRoomId();
        messagingTemplate.convertAndSend(destination, content);
    }
}
