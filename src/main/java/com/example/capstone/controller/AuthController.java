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

    @GetMapping("/api/oauth/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code) throws Exception {
        // 1. 카카오 access_token 요청
        String clientId = "e0f7c861c1363c8c05d661936397b603";
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
}
