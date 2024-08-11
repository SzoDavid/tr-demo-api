package com.example.trdemoapi.repository;

import com.example.trdemoapi.model.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    @Query("SELECT s FROM Subject s " +
            "WHERE NOT EXISTS (" +
            "SELECT sc FROM StudentCourse sc " +
            "WHERE sc.course.subject = s AND sc.student.id = :studentId)")
    Page<Subject> findAvailableSubjectsForUser(@Param("studentId") Long studentId, Pageable pageable);

}
