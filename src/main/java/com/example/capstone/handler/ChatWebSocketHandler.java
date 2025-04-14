package com.example.capstone.handler;

import com.example.capstone.dto.ChatMessageDto;
import com.example.capstone.service.UserQueueManager;
import com.example.capstone.service.DynamicRabbitListenerManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Spring WebSocket 방식의 핸들러
 */
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final UserQueueManager queueManager;
    private final DynamicRabbitListenerManager listenerManager;
    private final ObjectMapper objectMapper;

    // userId → WebSocketSession 저장
    private final Map<Integer, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public ChatWebSocketHandler(UserQueueManager queueManager,
                                DynamicRabbitListenerManager listenerManager,
                                ObjectMapper objectMapper) {
        this.queueManager = queueManager;
        this.listenerManager = listenerManager;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("WebSocket 연결 시도됨: " + session.getUri());

        try {
            // URI에서 userId 추출: /ws/chat/{userId}
            String path = session.getUri().getPath(); // 예: /ws/chat/1
            int userId = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));

            // 세션 등록
            sessions.put(userId, session);

            // 사용자 큐 및 리스너 등록
            queueManager.declareUserQueue(userId);
            listenerManager.startListening(userId);

            System.out.println("WebSocket 연결 성공 userId = " + userId);
        }
        catch (Exception e) {
            System.out.println("WebSocket 연결 중 오류 출력: " + e.getMessage());
            e.printStackTrace();
        }

    }

    // 메시지를 해당 userId에게 전송
    public void sendToUser(int userId, ChatMessageDto message) {
        WebSocketSession session = sessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message.getContent()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 연결 종료 시 세션 제거
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.values().remove(session); // 값 기반으로 제거
    }
}
