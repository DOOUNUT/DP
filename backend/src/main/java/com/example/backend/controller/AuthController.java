package com.example.backend.controller;

import com.example.backend.exception.UsernameNotFoundException;
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

    public static class LoginRequest {
        private String username;
        private String password;

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

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("입력된 username: " + loginRequest.getUsername());
        User user = userService.findByUsername(loginRequest.getUsername());
        Map<String, String> response = new HashMap<>();
    
        if (loginRequest.getPassword().equals(user.getPassword())) {
            response.put("message", "로그인 성공");
            response.put("username", user.getUsername());
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "로그인 실패: 비밀번호 오류");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    // UsernameNotFoundException 처리
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
