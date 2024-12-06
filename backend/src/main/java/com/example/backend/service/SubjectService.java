package com.example.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.backend.repository.SubjectRepository;
import com.example.backend.model.Subject;
import com.example.backend.dto.SubjectRequest;
import com.example.backend.model.User;

import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    public boolean addSubject(SubjectRequest request, User user) {
        try {
            Subject subject = new Subject();
            subject.setSubjectName(request.getSubjectName()); // subject_name 필드 설정
            subject.setProfessor(request.getProfessor());     // professor 필드 설정
            subject.setDescription(request.getDescription()); // description 필드 설정
            subject.setUser(user);  // user_id 설정

            subjectRepository.save(subject);  // 저장
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Subject> getSubjectsByUser(Long userId) {
        return subjectRepository.findByUserId(userId);  // user_id에 맞는 과목 목록 반환
    }
}
