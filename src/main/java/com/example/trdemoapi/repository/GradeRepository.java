package com.example.trdemoapi.repository;

import com.example.trdemoapi.model.Grade;
import com.example.trdemoapi.model.GradeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, GradeId> {
    @Query("SELECT AVG(g.grade) FROM Grade g WHERE g.student.id = :studentId")
    Optional<Double> getAverageGradeForStudent(@Param("studentId") Long studentId);
}
