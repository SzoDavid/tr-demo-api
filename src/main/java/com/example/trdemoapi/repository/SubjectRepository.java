package com.example.trdemoapi.repository;

import com.example.trdemoapi.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    @Query("SELECT s FROM Subject s " +
            "WHERE NOT EXISTS (" +
            "SELECT sc FROM StudentCourse sc " +
            "WHERE sc.course.subject = s AND sc.student.id = :studentId)")
    List<Subject> findAvailableSubjectsForUser(@Param("studentId") Long studentId);

}
