package com.example.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.security.Principal;
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

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<String> addSubject(@RequestBody SubjectRequest request, Principal principal) {
        String username = principal.getName();
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);

        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            User user = new User(); // UserEntity에서 User로 변환
            subjectService.addSubject(request, user);
            return ResponseEntity.ok("과목이 추가되었습니다.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
    }

    @GetMapping
    public ResponseEntity<List<Subject>> getSubjects(Principal principal) {
        String username = principal.getName();
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);

        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            User user = new User(userEntity); // UserEntity에서 User로 변환
            List<Subject> subjects = subjectService.getSubjectsByUser(user.getId());
            return ResponseEntity.ok(subjects);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
    }
}
