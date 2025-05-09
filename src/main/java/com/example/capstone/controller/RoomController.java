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
@RequestMapping("/rooms")
public class RoomController {
    private final ChatRoomRepository chatRoomRepository;
    private final RoomQueueManager roomQueueManager;
    private final ChatMessageConsumer chatMessageConsumer;

    // [POST] /rooms?opponentId=2
    // 로그인한 사용자가 creator, 상대방은 opponent로 등록
    @PostMapping
    public ResponseEntity<ChatRoom> createRoom(@RequestParam Long opponentId,
                                               @AuthenticationPrincipal UserDto userDto) {
        Long creatorId = (long) userDto.getUserId(); // 현재 로그인한 사용자의 userId 저장.

        ChatRoom room = new ChatRoom();
        room.setCreatorId(creatorId);
        room.setOpponentId(opponentId);
        room.setRoomLink(UUID.randomUUID().toString());
        chatRoomRepository.save(room);

        // 채팅방 단위 큐 생성 및 STOMP 리스너 시작
        roomQueueManager.declareRoomQueue(room.getRoomId());
        chatMessageConsumer.startListenerForRoom(room.getRoomId());

        System.out.println("방 생성 완료. 방 번호: " + room.getRoomId());
        System.out.println("로그인한 userId: " + userDto.getUserId());
        return ResponseEntity.ok(room);
    }

    // [GET] /rooms?link=roomLink
    // 초대 링크로 방 입장 → (초대 받은 사람의) userId 인증 필요
    @GetMapping(params = "link")
    public ResponseEntity<ChatRoom> getRoomByLink(@RequestParam String link,
                                                  @AuthenticationPrincipal UserDto userDto) {
        ChatRoom room = chatRoomRepository.findByRoomLink(link)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "방을 찾을 수 없습니다."));

        Long loginUserId = (long) userDto.getUserId();
        // (초대 받은 사람의) userId와 opponentId가 같아야 함.
        if (!room.getOpponentId().equals(loginUserId) && !room.getCreatorId().equals(loginUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "이 방에 접근할 권한이 없습니다.");
        }

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
