package com.example.capstone.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    // 비밀 키 설정
    private final Key key = Keys.hmacShaKeyFor(
            "very-secret-key-for-jwt-signing-1234567890123456".getBytes(StandardCharsets.UTF_8)
    );

    // Access Token 유효 기간: 60분
    private final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60;
    // Refresh Token 유효 기간: 7일
    private final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7;

    // Access Token 생성
    public String generateAccessToken(Long user_id, String nickname) {
        return Jwts.builder()
                .setSubject("AccessToken")
                .claim("user_id", user_id)
                .claim("nickname", nickname)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION)) // 만료 시각
                .signWith(key)
                .compact();
    }

    // Refresh Token 생성
    public String generateRefreshToken(Long user_id, String nickname) {
        return Jwts.builder()
                .setSubject("RefreshToken")
                .claim("user_id", user_id)
                .claim("nickname", nickname)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION)) // 만료 시각
                .signWith(key)
                .compact();
    }

     // 토큰 유효성 검증 -> 유효하면 true, 아니면 false
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)       // 서명 키로 검증
                    .build()
                    .parseClaimsJws(token);   // JWT 파싱 (검증 포함)
            return true;
        } catch (JwtException e) {
            return false; // 유효하지 않으면 false 반환
        }
    }
}
