package com.example.trdemoapi.repository;

import com.example.trdemoapi.model.Course;
import com.example.trdemoapi.model.StudentCourse;
import com.example.trdemoapi.model.StudentCourseId;
import com.example.trdemoapi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, StudentCourseId> {
    @Query("SELECT sc.course FROM StudentCourse sc WHERE sc.student.id = :studentId")
    Page<Course> findCoursesByStudentId(@Param("studentId") Long studentId, Pageable pageable);

    @Query("SELECT sc.student FROM StudentCourse sc WHERE sc.course.id = :courseId")
    Page<User> findStudentsByCourseId(@Param("courseId") Long courseId, Pageable pageable);

    @Query("SELECT COUNT(sc) > 0 FROM StudentCourse sc WHERE sc.student.id = :studentId AND sc.course.id = :courseId")
    boolean existsByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    @Query("SELECT COUNT(sc) > 0 FROM StudentCourse sc " +
            "JOIN sc.course c " +
            "WHERE sc.student.id = :studentId AND c.subject.id = :subjectId")
    boolean existsByStudentIdAndSubjectId(@Param("studentId") Long studentId, @Param("subjectId") Long subjectId);

    Optional<StudentCourse> findStudentCoursesById(StudentCourseId studentCourseId);
}
