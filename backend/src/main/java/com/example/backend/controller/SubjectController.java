package com.example.backend.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import com.example.backend.service.SubjectService;
import com.example.backend.repository.UserRepository;
import com.example.backend.model.Subject; // Subject 임포트
import com.example.backend.dto.SubjectRequest;
import com.example.backend.model.User; // User 임포트
import com.example.backend.entity.UserEntity; // UserEntity 임포트

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService subjectService;
    private final UserRepository userRepository;

    public SubjectController(SubjectService subjectService, UserRepository userRepository) {
        this.subjectService = subjectService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<String> addSubject(@RequestBody SubjectRequest request) {
        // 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // JWT 토큰에서 추출된 username

        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);

        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            User user = new User(userEntity); // UserEntity에서 User로 변환

            // 과목 추가
            boolean isAdded = subjectService.addSubject(request, user);

            if (isAdded) {
                return ResponseEntity.ok("과목이 추가되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("과목 추가에 실패했습니다.");
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
    }

    @GetMapping
    public ResponseEntity<List<Subject>> getSubjects() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // JWT 토큰에서 추출된 username

        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);

        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            User user = new User(userEntity); // UserEntity에서 User로 변환
            List<Subject> subjects = subjectService.getSubjectsByUser(user.getId());

            if (subjects != null && !subjects.isEmpty()) {
                return ResponseEntity.ok(subjects);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(subjects);
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
    }
}
