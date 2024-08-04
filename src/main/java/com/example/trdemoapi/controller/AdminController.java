package com.example.trdemoapi.controller;

import com.example.trdemoapi.dto.CreateUserReq;
import com.example.trdemoapi.dto.UpdateUserRolesReq;
import com.example.trdemoapi.dto.UserResp;
import com.example.trdemoapi.model.Course;
import com.example.trdemoapi.model.Subject;
import com.example.trdemoapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Operation(summary="All users", description="Returns with the details of all users")
    @GetMapping("/users")
    public ResponseEntity<List<UserResp>> getAllUsers() {
        var users = userService.allUsers().stream().map(UserResp::fromUser).collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @Operation(summary="Get user by id", description="Returns with the details of the user with the given id")
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResp> getUserById(@PathVariable Long id) {
        var user = UserResp.fromUser(userService.loadUserById(id));

        return ResponseEntity.ok().body(user);
    }

    @Operation(summary="Create user", description="Returns with the user created")
    @PostMapping("/users")
    public ResponseEntity<UserResp> createUser(@Valid @RequestBody CreateUserReq request) {
        var user = UserResp.fromUser(userService.createUser(request));
        return ResponseEntity.ok().body(user);
    }

    @Operation(summary="Update user roles", description="Updates the user's roles with the given list")
    @PutMapping("/users/{id}")
    public ResponseEntity<UserResp> updateUserRoles(@PathVariable Long id, @RequestBody UpdateUserRolesReq request) {
        var user = userService.loadUserById(id);
        var response = UserResp.fromUser(userService.updateUserRoles(user, request.getRoles()));

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary="Delete user", description="Deletes the user with the given id")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
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
