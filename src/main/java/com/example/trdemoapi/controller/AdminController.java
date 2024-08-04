package com.example.trdemoapi.controller;

import com.example.trdemoapi.dto.CreateUserReq;
import com.example.trdemoapi.model.Course;
import com.example.trdemoapi.model.Subject;
import com.example.trdemoapi.model.User;
import com.example.trdemoapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Validated
@RestController
@RequestMapping("/admin")
@Tag(name="Admin", description="the endpoints for authenticated admins")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    //region User
    @Operation(summary="All users", description="Returns with a details of all users")
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        var users = userService.allUsers();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        var user = userService.loadUserById(id);

        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserReq request) {
        var user = userService.createUser(request);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        var user = userService.loadUserById(id);
        userService.deleteUser(user);

        return ResponseEntity.ok().body("User deleted successfully");
    }
    //endregion

    //region Subject
    @GetMapping("/subjects")
    public ResponseEntity<?> getAllSubjects() {
        return ResponseEntity.ok().body(new ArrayList<Subject>());
    }

    @GetMapping("/subjects/{id}")
    public ResponseEntity<?> getSubjectById(@PathVariable Long id) {
        return ResponseEntity.ok().body(new Subject());
    }

    @PostMapping("/subjects")
    public ResponseEntity<?> createSubject(@RequestBody Subject subject) {
        return ResponseEntity.ok().body(subject);
    }

    @PutMapping("/subjects/{id}")
    public ResponseEntity<?> updateSubject(@PathVariable Long id, @RequestBody Subject subject) {
        return ResponseEntity.ok().body(subject);
    }

    @DeleteMapping("/subjects/{id}")
    public ResponseEntity<?> deleteSubject(@PathVariable Long id) {
        return ResponseEntity.ok().body("delete subject");
    }

    @PostMapping("/subjects/{id}/courses")
    public ResponseEntity<?> addCourseToSubject(@PathVariable Long id, @RequestBody Course course) {
        return ResponseEntity.ok().body(course);
    }

    @GetMapping("/subjects/{id}/courses")
    public ResponseEntity<?> getCoursesBySubjectId(@PathVariable Long id) {
        return ResponseEntity.ok().body(new ArrayList<Course>());
    }
    //endregion

    //region Course
    @GetMapping("/courses/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok().body(new Course());
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        return ResponseEntity.ok().body(course);
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        return ResponseEntity.ok().body("delete course");
    }
    //endregion
}
