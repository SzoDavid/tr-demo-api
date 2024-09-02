package com.example.trdemoapi.service;

import com.example.trdemoapi.model.Course;
import com.example.trdemoapi.model.Grade;
import com.example.trdemoapi.model.GradeId;
import com.example.trdemoapi.model.User;
import com.example.trdemoapi.repository.GradeRepository;
import com.example.trdemoapi.repository.StudentCourseRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Transactional
public class GradeService {
    private final GradeRepository gradeRepository;
    private final StudentCourseRepository studentCourseRepository;

    public GradeService(GradeRepository gradeRepository, StudentCourseRepository studentCourseRepository) {
        this.gradeRepository = gradeRepository;
        this.studentCourseRepository = studentCourseRepository;
    }

    public Double getAverageGradeForStudent(User student) {
        return gradeRepository.getAverageGradeForStudent(student.getId()).orElse(null);
    }

    @Transactional
    public Grade gradeStudent(Course course, User student, int grade) {
        var isRegistered = studentCourseRepository.existsByStudentIdAndCourseId(student.getId(), course.getId());

        if (!isRegistered) {
            throw new IllegalArgumentException("Student with id " + student.getId() + " is not registered");
        }

        var gradeId = new GradeId().setStudentId(student.getId())
                                   .setCourseId(course.getId());

        var gradeObj = new Grade()
                .setId(gradeId)
                .setStudent(student)
                .setCourse(course)
                .setGrade(BigDecimal.valueOf(grade));

        return gradeRepository.save(gradeObj);
    }
}
