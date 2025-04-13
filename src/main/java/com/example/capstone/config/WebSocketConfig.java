package com.example.capstone.config;

import com.example.capstone.handler.ChatWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

/**
 * Spring WebSocket 설정 클래스.
 * WebSocket 엔드포인트를 등록하고, 해당 핸들러를 지정하는 역할을 함.
 */
@Configuration
@EnableWebSocket // Spring WebSocket 기능 활성화
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatWebSocketHandler handler;

    /**
     * WebSocket 핸들러를 생성자 주입으로 받아서 등록할 준비.
     *
     * @param handler 실제 WebSocket 연결을 처리하는 핸들러
     */
    public WebSocketConfig(ChatWebSocketHandler handler) {
        this.handler = handler;
    }

    /**
     * WebSocket 경로를 등록하는 메서드.
     * 클라이언트는 ws://localhost:8080/ws/chat/{userId} 형식으로 WebSocket 연결 가능.
     *
     * @param registry WebSocket 핸들러 레지스트리
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, "/ws/chat/{userId}")
                .setAllowedOrigins("*");                  // 모든 도메인에서 접속 허용 (개발용)
    }
}
