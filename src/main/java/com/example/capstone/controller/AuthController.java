package com.example.capstone.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class AuthController {
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/api/oauth/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code) throws Exception {
        String clientId = "e0f7c861c1363c8c05d661936397b603";
        String redirectUri = "http://localhost:8080/oauth/kakao/callback";

        System.out.println("프론트에서 받은 code: " + code);

        // 1. 카카오에 access_token 요청
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // Content-Type 설정

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code"); // 고정값
        params.add("client_id", clientId);              // REST API 키
        params.add("redirect_uri", redirectUri);        // 인가 요청 시 사용한 redirect URI와 동일해야 함
        params.add("code", code);                       // 프론트에서 받은 인가 코드

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        // 2. access_token 요청
        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://kauth.kakao.com/oauth/token",
                request,
                String.class
        );

        // 3. access_token 추출
        ObjectMapper mapper = new ObjectMapper();
        JsonNode tokenJson = mapper.readTree(response.getBody());
        String accessToken = tokenJson.get("access_token").asText();

        // 4. access_token을 사용해서 사용자 정보 요청
        HttpHeaders profileHeaders = new HttpHeaders();
        profileHeaders.add("Authorization", "Bearer " + accessToken); // Authorization 헤더에 토큰 추가
        HttpEntity<?> profileRequest = new HttpEntity<>(profileHeaders);

        ResponseEntity<String> profileResponse = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                profileRequest,
                String.class
        );

//        // 5. 사용자 정보 파싱
//        JsonNode profileJson = mapper.readTree(profileResponse.getBody());
//        String kakaoEmail = profileJson.path("kakao_account").path("email").asText();
//
//        // 6. email 등을 이용해 DB 사용자 확인 또는 회원가입 처리
//        // TODO: DB에서 해당 이메일로 사용자를 찾거나, 없으면 새로 생성
//
//        // 7. JWT 발급 또는 세션 처리
//        // TODO: JWT 발급 로직 추가
//        return ResponseEntity.ok(Map.of("accessToken", "발급한 우리 JWT"));

        /**
         * 일단 BD 저장 말고 로그 출력
         */
        // 사용자 정보 파싱 및 출력
        JsonNode profileJson = mapper.readTree(profileResponse.getBody());

        String email = profileJson.path("kakao_account").path("email").asText();
        String nickname = profileJson.path("properties").path("nickname").asText();

        System.out.println("카카오 사용자 이메일: " + email);
        System.out.println("카카오 사용자 닉네임: " + nickname);

        // 사용자 정보를 로그에 출력
        return ResponseEntity.ok("사용자 정보가 서버에 출력되었습니다.");
    }

}
