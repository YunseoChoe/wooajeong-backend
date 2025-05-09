package com.example.capstone.controller;

import com.example.capstone.domain.User;
import com.example.capstone.jwt.JwtUtil;
import com.example.capstone.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    // 카카오 로그인
    @GetMapping("/api/oauth/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code) throws Exception {

        System.out.println("카카오 로그인 시작.");
        // 1. 카카오 access_token 요청
        String clientId = "e0f7c861c1363c8c05d661936397b603"; // REST API KEY
        String redirectUri = "https://www.yunseo.store/oauth/kakao/callback";
//        String redirectUri = "http://localhost:8080/oauth/kakao/callback";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://kauth.kakao.com/oauth/token", request, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode tokenJson = mapper.readTree(response.getBody());
        String kakaoAccessToken = tokenJson.get("access_token").asText();

        // 2. 사용자 정보 요청
        HttpHeaders profileHeaders = new HttpHeaders();
        profileHeaders.add("Authorization", "Bearer " + kakaoAccessToken);
        HttpEntity<?> profileRequest = new HttpEntity<>(profileHeaders);

        ResponseEntity<String> profileResponse = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                profileRequest,
                String.class);

        JsonNode profileJson = mapper.readTree(profileResponse.getBody());
        String kakaoEmail = profileJson.path("kakao_account").path("email").asText();
        String nickname = profileJson.path("properties").path("nickname").asText();

        System.out.println("사용자 이메일: " + kakaoEmail);
        System.out.println("사용자 닉네임: " + nickname);

        // 3. 사용자 저장 or 조회
        User user = userRepository.findByEmail(kakaoEmail)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(kakaoEmail);
                    newUser.setNickname(nickname);
                    System.out.println("DB 저장 성공.");
                    return userRepository.save(newUser);
                });

        // 4. JWT 발급
        String accessToken = jwtUtil.generateAccessToken(user.getUser_id(), user.getNickname());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUser_id(), user.getNickname());

        System.out.println("발급 받은 accessToken: " + accessToken);
        System.out.println("발급 받은 refreshToken: " + refreshToken);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return ResponseEntity.ok(tokens);
    }

    // 회원 정보 반환
    @GetMapping("/api/user")
    public ResponseEntity<?> getMyProfile(@RequestHeader("Authorization") String bearerToken) {
        try {
            // "Bearer" 제거하여 실제 토큰 추출
            String token = bearerToken.replace("Bearer ", "").trim();

            // JWT에서 user_id 추출
            Long userId = jwtUtil.getUserIdFromToken(token);
            System.out.println("추출한 user_id: " + userId);

            // DB에서 사용자 조회
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("user_id", user.getUser_id());
            userInfo.put("email", user.getEmail());
            userInfo.put("nickname", user.getNickname());

            return ResponseEntity.ok(userInfo);

        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }
    }

    // 회원 탈퇴
    @DeleteMapping("/api/user/delete")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String bearerToken) {
        try {
            String token = bearerToken.replace("Bearer ", "").trim();
            Long userId = jwtUtil.getUserIdFromToken(token);
            System.out.println("추출한 user_id: " + userId);

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

            userRepository.delete(user);
            return ResponseEntity.ok("회원탈퇴가 완료되었습니다.");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }
    }



}
