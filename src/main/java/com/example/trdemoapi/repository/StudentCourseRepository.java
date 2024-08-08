package com.example.trdemoapi.repository;

import com.example.trdemoapi.model.Course;
import com.example.trdemoapi.model.StudentCourse;
import com.example.trdemoapi.model.StudentCourseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, StudentCourseId> {
    @Query("SELECT sc.course FROM StudentCourse sc WHERE sc.student.id = :studentId")
    List<Course> findCoursesByStudentId(@Param("studentId") Long studentId);

    Optional<StudentCourse> findStudentCoursesById(StudentCourseId studentCourseId);
}
