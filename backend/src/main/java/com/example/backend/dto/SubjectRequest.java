package com.example.backend.dto;

public class SubjectRequest {

    private String subjectName;  // subject_name 필드에 매핑
    private String professor;    // professor 필드에 매핑
    private String description;  // description 필드에 매핑

    // Getters and setters
    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
