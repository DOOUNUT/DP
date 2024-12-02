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

    public void addSubject(SubjectRequest request, User user) {
        Subject subject = new Subject();
        subject.setSubjectName(request.getSubjectName());
        subject.setProfessor(request.getProfessor());
        subject.setUser(user);
        subjectRepository.save(subject);
    }

    public List<Subject> getSubjectsByUser(Long userId) {
        return subjectRepository.findByUserId(userId);
    }
}
