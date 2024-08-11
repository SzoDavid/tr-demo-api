package com.example.trdemoapi.repository;

import com.example.trdemoapi.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT c FROM Course c WHERE c.teacher.id = :teacherId")
    Page<Course> findCoursesByTeacherId(@Param("teacherId") Long teacherId, Pageable pageable);
}
