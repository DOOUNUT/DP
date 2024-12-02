package com.example.backend.repository;

import com.example.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // users 테이블에서 username으로 검색
    Optional<UserEntity> findByUsername(String username);
}
