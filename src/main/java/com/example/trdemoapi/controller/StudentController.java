package com.example.trdemoapi.controller;

import com.example.trdemoapi.dto.StudentAverageResp;
import com.example.trdemoapi.model.Course;
import com.example.trdemoapi.model.Subject;
import com.example.trdemoapi.service.CourseService;
import com.example.trdemoapi.service.GradeService;
import com.example.trdemoapi.service.SubjectService;
import com.example.trdemoapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/student")
@Tag(name="Student", description="the endpoints for authenticated students")
public class StudentController {
    private final CourseService courseService;
    private final UserService userService;
    private final GradeService gradeService;
    private final SubjectService subjectService;

    public StudentController(CourseService courseService, UserService userService, GradeService gradeService, SubjectService subjectService) {
        this.courseService = courseService;
        this.userService = userService;
        this.gradeService = gradeService;
        this.subjectService = subjectService;
    }

    @Operation(summary="All available courses", description="Returns with all of the subjects available for the " +
            "authenticated user. A subject is available if the user is not registered for any of its courses.")
    @GetMapping("/available")
    public ResponseEntity<Page<Subject>> getAvailableSubjects(@RequestParam(value = "offset", required = false) Integer offset,
                                                              @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                              @RequestParam(value = "sortBy", required = false) String sortBy) {
        if (offset == null) offset = 0;
        if (pageSize == null) pageSize = 10;
        if (StringUtils.isEmpty(sortBy)) sortBy ="id";

        var currentUser = userService.loadCurrentUser();
        var subjects = subjectService.loadAvailableSubjectsForUser(currentUser, PageRequest.of(offset, pageSize, Sort.by(sortBy)));
        return ResponseEntity.ok().body(subjects);
    }

    @Operation(summary="All taken courses", description="Returns with all of the courses taken by the authenticated " +
            "user.")
    @GetMapping("/taken-courses")
    public ResponseEntity<Page<Course>> getTakenCourses(@RequestParam(value = "offset", required = false) Integer offset,
                                                        @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                        @RequestParam(value = "sortBy", required = false) String sortBy) {
        if (offset == null) offset = 0;
        if (pageSize == null) pageSize = 10;
        if (StringUtils.isEmpty(sortBy)) sortBy ="id";

        var currentUser = userService.loadCurrentUser();
        var courses = courseService.loadAllCoursesForStudent(currentUser, PageRequest.of(offset, pageSize, Sort.by(sortBy)));

        return ResponseEntity.ok().body(courses);
    }

    @Operation(summary="Register student for course", description="Returns with all of the courses taken by the " +
            "authenticated user.")
    @PostMapping("/taken-courses")
    public ResponseEntity<String> takeCourse(@RequestBody Long courseId) {
        var currentUser = userService.loadCurrentUser();
        var course = courseService.loadCourseById(courseId);
        courseService.registerUserOnCourse(currentUser, course);

        return ResponseEntity.ok().body("User registered successfully");
    }

    @Operation(summary="Unregister student from course", description="Returns with all of the courses taken by the " +
            "authenticated user.")
    @DeleteMapping("/taken-courses/{courseId}")
    public ResponseEntity<String> dropCourse(@PathVariable Long courseId) {
        var currentUser = userService.loadCurrentUser();
        var course = courseService.loadCourseById(courseId);
        courseService.unregisterUserOnCourse(currentUser, course);

        return ResponseEntity.ok().body("User unregistered successfully");
    }

    @Operation(summary="Average of student", description="If the user has grades, the average will be returned, " +
            "otherwise the hasAverage property will be false.")
    @GetMapping("/average")
    public ResponseEntity<StudentAverageResp> getAverage() {
        var currentUser = userService.loadCurrentUser();

        var average = gradeService.getAverageGradeForStudent(currentUser);
        var response = new StudentAverageResp(average, average == null);

        return ResponseEntity.ok().body(response);
    }
}
