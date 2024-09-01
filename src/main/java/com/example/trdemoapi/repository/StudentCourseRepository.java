package com.example.trdemoapi.repository;

import com.example.trdemoapi.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, StudentCourseId> {
    @Query("SELECT sc.course FROM StudentCourse sc WHERE sc.student.id = :studentId")
    Page<Course> findCoursesByStudentId(@Param("studentId") Long studentId, Pageable pageable);

    @Query("SELECT sc.course FROM StudentCourse sc WHERE sc.student.id = :studentId")
    List<Course> findCoursesByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT new com.example.trdemoapi.model.Student(sc.student, g.grade) " +
            "FROM StudentCourse sc LEFT JOIN Grade g " +
            "ON g.id.courseId = sc.course.id AND g.id.studentId = sc.student.id " +
            "WHERE sc.course.id = :courseId")
    Page<Student> findStudentsByCourseId(@Param("courseId") Long courseId, Pageable pageable);

    @Query("SELECT new com.example.trdemoapi.model.Student(sc.student, g.grade) " +
            "FROM StudentCourse sc LEFT JOIN Grade g " +
            "ON g.id.courseId = sc.course.id AND g.id.studentId = sc.student.id " +
            "WHERE sc.course.id = :courseId " +
            "ORDER BY sc.student.name")
    List<Student> findAllStudentsByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT COUNT(sc) > 0 FROM StudentCourse sc WHERE sc.student.id = :studentId AND sc.course.id = :courseId")
    boolean existsByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    @Query("SELECT c FROM StudentCourse sc " +
            "JOIN sc.course c " +
            "WHERE sc.student.id = :studentId AND c.subject.id = :subjectId")
    Optional<Course> findCourseByStudentIdAndSubjectId(@Param("studentId") Long studentId, @Param("subjectId") Long subjectId);

    @Query("SELECT COUNT(sc) > 0 FROM StudentCourse sc " +
            "JOIN sc.course c " +
            "WHERE sc.student.id = :studentId AND c.subject.id = :subjectId")
    boolean existsByStudentIdAndSubjectId(@Param("studentId") Long studentId, @Param("subjectId") Long subjectId);

    Optional<StudentCourse> findStudentCoursesById(StudentCourseId studentCourseId);
}
