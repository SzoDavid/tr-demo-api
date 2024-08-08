package com.example.trdemoapi.repository;

import com.example.trdemoapi.model.Course;
import com.example.trdemoapi.model.StudentCourse;
import com.example.trdemoapi.model.StudentCourseId;
import com.example.trdemoapi.model.User;
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

    @Query("SELECT sc.student FROM StudentCourse sc WHERE sc.course.id = :courseId")
    List<User> findStudentsByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT COUNT(sc) > 0 FROM StudentCourse sc WHERE sc.student.id = :studentId AND sc.course.id = :courseId")
    boolean existsByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    Optional<StudentCourse> findStudentCoursesById(StudentCourseId studentCourseId);
}
