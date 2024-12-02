package com.example.backend.service;

import com.example.backend.entity.UserEntity;
import com.example.backend.exception.UsernameNotFoundException;
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
        // 데이터베이스에서 username에 해당하는 사용자 조회
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);
        // UserEntity가 존재하면 User로 변환하고, 없으면 예외를 던집니다.
        UserEntity userEntity = userEntityOptional.orElseThrow(() -> new UsernameNotFoundException("사용자 정보 없음: " + username));
        return convertToUser(userEntity);
    }

    // UserEntity를 User로 변환하는 메서드
    private User convertToUser(UserEntity userEntity) {
        User user = new User();
        user.setUsername(userEntity.getUsername());
        user.setPassword(userEntity.getPassword());
        return user;
    }
}
