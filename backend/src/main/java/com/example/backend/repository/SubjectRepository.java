package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.model.Subject;
import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findByUserId(Long userId);
}
