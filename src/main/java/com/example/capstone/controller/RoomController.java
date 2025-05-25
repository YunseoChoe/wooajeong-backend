package com.example.capstone.controller;

import com.example.capstone.domain.ChatRoom;
import com.example.capstone.listener.ChatMessageConsumer;
import com.example.capstone.repository.ChatRoomRepository;
import com.example.capstone.service.RoomQueueManager;
import com.example.capstone.dto.UserDto;
import com.example.capstone.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
public class RoomController {
    private final ChatRoomRepository chatRoomRepository;
    private final RoomQueueManager roomQueueManager;
    private final ChatMessageConsumer chatMessageConsumer;

    // [POST] /room/make?productId=2
    @PostMapping("/make")
    public ResponseEntity<ChatRoom> createRoom(@RequestParam Long productId,
                                               @AuthenticationPrincipal UserDto userDto) {
        System.out.println("방 만들기 api 시작.");
        Long creatorId = (long) userDto.getUserId(); // 현재 로그인한 사용자의 userId 저장.

        ChatRoom room = new ChatRoom();
        room.setRoomId(productId); // room_id는 productId값으로 저장.
        room.setRoomLink(UUID.randomUUID().toString()); // room_link
        room.setCreatorId(creatorId); // creator_id
        chatRoomRepository.save(room);

        // 채팅방 단위 큐 생성 및 STOMP 리스너 시작
        roomQueueManager.declareRoomQueue(room.getRoomId());
        chatMessageConsumer.startListenerForRoom(room.getRoomId());

        System.out.println("방 생성 완료. 방 번호: " + room.getRoomId());
        System.out.println("로그인한 userId: " + userDto.getUserId());
        return ResponseEntity.ok(room);
    }

    // [GET] /room/join?link=roomLink.
    // 누구나 입장 가능한 채팅방 링크 조회 (인증 불필요)
    @GetMapping("/join")
    public ResponseEntity<ChatRoom> getRoomByLink(@RequestParam String link) {
        System.out.println("방 조회 api 시작.");
        ChatRoom room = chatRoomRepository.findByRoomLink(link)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "방을 찾을 수 없습니다."));

        return ResponseEntity.ok(room);
    }

    // [GET] /rooms/my
    // 로그인한 사용자가 참여 중인 모든 채팅방 조회
    @GetMapping("/my")
    public ResponseEntity<List<ChatRoom>> getMyRooms(@AuthenticationPrincipal UserDto userDto) {
        Long userId = (long) userDto.getUserId();
        List<ChatRoom> rooms = chatRoomRepository.findAllByCreatorIdOrOpponentId(userId, userId);
        return ResponseEntity.ok(rooms);
    }
}

