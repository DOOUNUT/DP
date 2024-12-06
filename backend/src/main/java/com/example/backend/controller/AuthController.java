package com.example.backend.controller;

import com.example.backend.exception.UsernameNotFoundException;
import com.example.backend.model.User;
import com.example.backend.service.UserService;
import com.example.backend.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

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
        User user = userService.findByUsername(loginRequest.getUsername());
        Map<String, String> response = new HashMap<>();

        if (user != null && loginRequest.getPassword().equals(user.getPassword())) {
            // Access Token 및 Refresh Token 생성
            String accessToken = jwtTokenProvider.generateToken(user.getUsername(), user.getRoles());
            String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

            response.put("message", "로그인 성공");
            response.put("username", user.getUsername());
            response.put("accessToken", accessToken);
            response.put("refreshToken", refreshToken);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "로그인 실패: 비밀번호 오류");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    // 토큰 재발급 API
    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        Map<String, String> response = new HashMap<>();

        if (jwtTokenProvider.validateToken(refreshToken)) {
            String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
            List<String> roles = jwtTokenProvider.getAuthoritiesFromToken(refreshToken)
                                                 .stream().map(Object::toString).toList();

            // 새로운 Access Token 발급
            String newAccessToken = jwtTokenProvider.generateToken(username, roles);
            response.put("accessToken", newAccessToken);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Invalid refresh token");
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
