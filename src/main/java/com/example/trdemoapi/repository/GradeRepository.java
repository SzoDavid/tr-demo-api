package com.example.trdemoapi.repository;

import com.example.trdemoapi.model.Grade;
import com.example.trdemoapi.model.GradeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeRepository extends JpaRepository<Grade, GradeId> {
}
