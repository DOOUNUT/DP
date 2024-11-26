package com.example.backend.service;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByUsername(String username) {
        // Optional을 사용하여 null을 처리합니다.
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElse(null); // 없으면 null을 반환
    }
}
