package com.example.backend.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/shareScreen")
    public void shareScreen(String screenData) {
        // 모든 클라이언트에게 화면 데이터 전송
        messagingTemplate.convertAndSend("/topic/screen", screenData);
    }
}
