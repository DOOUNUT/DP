package com.example.backend.controller;

import com.example.backend.model.Message; // Message 클래스 import
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:3000")
public class MessageController {

    private List<Message> messages = new ArrayList<>(); // 메시지 저장 리스트

    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
        messages.add(message); // 메시지 저장
        return ResponseEntity.ok(message); // 저장된 메시지 반환
    }

    @GetMapping
    public ResponseEntity<List<Message>> getMessages() {
        return ResponseEntity.ok(messages); // 저장된 메시지 목록 반환
    }
}
