package com.example.trdemoapi.repository;

import com.example.trdemoapi.model.Course;
import com.example.trdemoapi.model.StudentCourse;
import com.example.trdemoapi.model.StudentCourseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, StudentCourseId> {
    List<Course> findCoursesByStudentId(Long studentId);
}
