package com.example.trdemoapi.controller;

import com.example.trdemoapi.model.Course;
import com.example.trdemoapi.model.Subject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/student")
public class StudentController {

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableCourses() {
        return ResponseEntity.ok().body(new ArrayList<Subject>());
    }

    @GetMapping("/taken-courses")
    public ResponseEntity<?> getTakenCourses() {
        return ResponseEntity.ok().body(new ArrayList<Course>());
    }

    @PostMapping("/taken-courses")
    public ResponseEntity<?> takeCourse(@RequestBody Long courseId) {
        return ResponseEntity.ok().body("take course");
    }

    @DeleteMapping("/taken-courses/{courseId}")
    public ResponseEntity<?> dropCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok().body("drop course");
    }

    @GetMapping("/average")
    public ResponseEntity<?> getAverage() {
        return ResponseEntity.ok().body("average");
    }
}
