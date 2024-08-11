package com.example.trdemoapi.controller;

import com.example.trdemoapi.model.Course;
import com.example.trdemoapi.model.User;
import com.example.trdemoapi.dto.GradeReq;
import com.example.trdemoapi.service.CourseService;
import com.example.trdemoapi.service.GradeService;
import com.example.trdemoapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/teacher")
@Tag(name="Teacher", description="the endpoints for authenticated teachers")
public class TeacherController {
    private final UserService userService;
    private final CourseService courseService;
    private final GradeService gradeService;

    public TeacherController(UserService userService, CourseService courseService, GradeService gradeService) {
        this.userService = userService;
        this.courseService = courseService;
        this.gradeService = gradeService;
    }

    @Operation(summary="All assigned courses", description="Returns with all of the courses where the authenticated " +
            "user is assigned as a teacher.")
    @GetMapping("/courses")
    public ResponseEntity<Page<Course>> getAssignedCourses(@RequestParam(value = "offset", required = false) Integer offset,
                                                           @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                           @RequestParam(value = "sortBy", required = false) String sortBy) {
        if (offset == null) offset = 0;
        if (pageSize == null) pageSize = 10;
        if (StringUtils.isEmpty(sortBy)) sortBy ="id";

        var currentUser = userService.loadCurrentUser();
        var courses = courseService.loadAllCoursesForTeacher(currentUser, PageRequest.of(offset, pageSize, Sort.by(sortBy)));
        return ResponseEntity.ok().body(courses);
    }

    @Operation(summary="Get students", description="Returns with all of the users registered to the given course.")
    @GetMapping("/courses/{courseId}/students")
    public ResponseEntity<Page<User>> getStudents(@PathVariable Long courseId,
                                                  @RequestParam(value = "offset", required = false) Integer offset,
                                                  @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                  @RequestParam(value = "sortBy", required = false) String sortBy) {
        var course = courseService.loadCourseById(courseId);
        var currentUser = userService.loadCurrentUser();

        if (!course.getTeacher().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }

        if (offset == null) offset = 0;
        if (pageSize == null) pageSize = 10;
        if (StringUtils.isEmpty(sortBy)) sortBy ="id";

        var students = userService.loadUsersByCourse(course, PageRequest.of(offset, pageSize, Sort.by(sortBy)));

        return ResponseEntity.ok().body(students);
    }

    @Operation(summary="Grade students", description="Grade one or more students.")
    @PostMapping("/courses/{courseId}/grades")
    public ResponseEntity<String> gradeStudents(@PathVariable Long courseId, @Valid @RequestBody GradeReq gradeRequest) {
        var course = courseService.loadCourseById(courseId);
        var currentUser = userService.loadCurrentUser();

        if (!course.getTeacher().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }

        for (var grade : gradeRequest.getGrades().entrySet()) {
            var user = userService.loadUserById(grade.getKey());
            gradeService.gradeStudent(course, user, grade.getValue());
        }

        return ResponseEntity.ok().body("Grades saved successfully");
    }

    @Operation(summary="Remove student from course")
    @DeleteMapping("/courses/{courseId}/students/{studentId}")
    public ResponseEntity<String> removeStudentFromCourse(@PathVariable Long courseId, @PathVariable Long studentId) {
        var course = courseService.loadCourseById(courseId);
        var currentUser = userService.loadCurrentUser();

        if (!course.getTeacher().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }

        var student = userService.loadUserById(studentId);
        courseService.unregisterUserOnCourse(student, course);

        return ResponseEntity.ok().body("Student removed successfully");
    }
}
