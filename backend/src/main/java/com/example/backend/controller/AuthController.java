package com.example.backend.controller;

import com.example.backend.model.User;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // 로그인 요청을 받는 DTO 클래스
    public static class LoginRequest {
        private String username;
        private String password;

        // Getter and Setter
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    // 로그인 처리
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.findByUsername(loginRequest.getUsername());

        Map<String, String> response = new HashMap<>();

        if (user == null) {
            response.put("message", "로그인 실패: 사용자 정보 없음");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);  // 사용자 없으면 404 반환
        }

        // 비밀번호를 단순 비교 (암호화 없이)
        if (loginRequest.getPassword().equals(user.getPassword())) {
            response.put("message", "로그인 성공");
            response.put("username", user.getUsername());  // 로그인한 사용자 이름 추가
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "로그인 실패: 비밀번호 오류");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);  // 비밀번호 오류시 401 반환
        }
    }
}
