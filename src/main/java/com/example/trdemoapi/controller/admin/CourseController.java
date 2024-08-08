package com.example.trdemoapi.controller.admin;

import com.example.trdemoapi.dto.UpdateCourseReq;
import com.example.trdemoapi.model.Course;
import com.example.trdemoapi.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/admin/courses")
@Tag(name="Course", description="the endpoints for authenticated admins to manage courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @Operation(summary="Get course by id", description="Returns with the details of the course with the given id.")
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        var course = courseService.loadCourseById(id);
        return ResponseEntity.ok().body(course);
    }

    @Operation(summary="Update course", description="Returns with the updated course.")
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @Valid @RequestBody UpdateCourseReq request) {
        var course = courseService.updateCourse(id, request);
        return ResponseEntity.ok().body(course);
    }

    @Operation(summary="Delete course", description="Deletes the course with the given id.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        var subject = courseService.loadCourseById(id);
        courseService.deleteCourse(subject);

        return ResponseEntity.ok().body("Course deleted successfully");
    }
}
