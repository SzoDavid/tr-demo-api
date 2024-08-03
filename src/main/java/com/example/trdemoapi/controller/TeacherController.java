package com.example.trdemoapi.controller;

import com.example.trdemoapi.model.Course;
import com.example.trdemoapi.model.User;
import com.example.trdemoapi.dto.GradeReq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @GetMapping("/courses")
    public ResponseEntity<?> getAssignedCourses() {
        return ResponseEntity.ok().body(new ArrayList<Course>());
    }

    @GetMapping("/courses/{courseId}/students")
    public ResponseEntity<?> getStudents(@PathVariable Long courseId) {
        return ResponseEntity.ok().body(new ArrayList<User>());
    }

    @PostMapping("/courses/{courseId}/grades")
    public ResponseEntity<?> gradeStudents(@PathVariable Long courseId, @RequestBody GradeReq gradeRequest) {
        return ResponseEntity.ok().body("graded");
    }

    @DeleteMapping("/courses/{courseId}/students/{studentId}")
    public ResponseEntity<?> removeStudentFromCourse(@PathVariable Long courseId, @PathVariable Long studentId) {
        return ResponseEntity.ok().body("removed");
    }
}
