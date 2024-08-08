package com.example.trdemoapi.service;

import com.example.trdemoapi.model.User;
import com.example.trdemoapi.repository.GradeRepository;
import org.springframework.stereotype.Service;

@Service
public class GradeService {
    private final GradeRepository gradeRepository;

    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public Double getAverageGradeForStudent(User student) {
        return gradeRepository.getAverageGradeForStudent(student.getId()).orElse(null);
    }
}
