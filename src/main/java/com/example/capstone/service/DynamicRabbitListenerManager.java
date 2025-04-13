package com.example.capstone.service;

import com.example.capstone.dto.ChatMessageDto;
import com.example.capstone.handler.ChatWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Integer.parseInt;

/**
 * userId기반으로 RabbitMQ 큐를 동적으로 구독하는 리스너(Consumer)를 등록.
 *
 * Spring에서 기본 제공하는 @RabbitListener는 정적 큐에만 사용할 수 있기 때문에,
 * SimpleMessageListenerContainer를 사용하여 큐 이름을 동적으로 지정하고 메시지를 수신함.
 */
@Service
public class DynamicRabbitListenerManager {

    private final ConnectionFactory connectionFactory;
    private final ChatWebSocketHandler handler;
    private final ObjectMapper objectMapper;

    // userId -> 해당 유저의 리스너 컨테이너 저장용 맵
    private final Map<Integer, SimpleMessageListenerContainer> listenerMap = new ConcurrentHashMap<>();

    public DynamicRabbitListenerManager(ConnectionFactory connectionFactory,
                                        @Lazy ChatWebSocketHandler handler,
                                        ObjectMapper objectMapper) {
        this.connectionFactory = connectionFactory;
        this.handler = handler;
        this.objectMapper = objectMapper;
    }

    /**
     * 주어진 userId에 대해 RabbitMQ 큐(chat.queue.{userId})를 구독하는 리스너를 생성 및 시작.
     * 이미 리스너가 등록된 경우 중복으로 생성하지 않음.
     *
     * @param userId 유저 고유 ID
     */
    public void startListening(int userId) {
        // 이미 리스너가 존재하면 중복 등록 방지
        if (listenerMap.containsKey(userId)) return;

        String queueName = "chat.queue." + userId;

        // 새 리스너 컨테이너 생성
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames(queueName); // 해당 유저의 큐 이름 지정

        // 메시지 수신 시 처리할 로직 정의
        container.setMessageListener((Message message) -> {
            try {
                // JSON 메시지를 ChatMessageDto 객체로 변환
                ChatMessageDto dto = objectMapper.readValue(message.getBody(), ChatMessageDto.class);

                // 해당 유저에게 WebSocket을 통해 메시지 전송
                handler.sendToUser(Integer.parseInt(dto.getReceiverId()), dto);
            } catch (IOException e) {
                e.printStackTrace(); // 에러 로그 출력
            }
        });

        container.start(); // 리스너 시작
        listenerMap.put(userId, container); // 맵에 리스너 등록
    }

    /**
     * 리스너 중지 및 제거 (선택 기능: 사용자 연결 종료 시 호출 가능)
     *
     * @param userId 유저 고유 ID
     */
    public void stopListening(int userId) {
        SimpleMessageListenerContainer container = listenerMap.remove(userId);
        if (container != null) {
            container.stop();
        }
    }
}
