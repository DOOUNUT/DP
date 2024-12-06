package com.example.backend.model;

import com.example.backend.entity.UserEntity;  // UserEntity import 추가
import com.example.backend.entity.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")  // 테이블 이름을 명시적으로 지정
public class User {

    @Id
    private Long id;
    private String username;
    private String password;
    private List<String> roles;  // roles 필드 추가

    // 기본 생성자
    public User() {}

    // 모든 필드를 포함하는 생성자
    public User(Long id, String username, String password, List<String> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    // UserEntity를 받아 User 객체로 변환하는 생성자
    public User(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.username = userEntity.getUsername();
        this.password = userEntity.getPassword();
        // roles를 List<Role>에서 List<String>으로 변환
        this.roles = userEntity.getRoles().stream()
                               .map(Role::getName)  // 각 Role 객체에서 이름을 가져옴
                               .collect(Collectors.toList());
    }

    // Getter 및 Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<String> getRoles() {  // getRoles 메서드 추가
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
