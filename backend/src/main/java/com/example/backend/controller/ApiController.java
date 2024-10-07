package com.example.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    // 예시로 간단한 메시지를 반환하는 API 작성
    @GetMapping("/message")
    public String getMessage() {
        return "Hello from the API!";
    }

    // 여기에 다른 API 메서드 추가
}
