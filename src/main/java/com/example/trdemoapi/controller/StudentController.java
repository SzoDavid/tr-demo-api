package com.example.trdemoapi.controller;

import com.example.trdemoapi.dto.StudentAverageResp;
import com.example.trdemoapi.dto.SuccessResp;
import com.example.trdemoapi.dto.timetable.TimetableResp;
import com.example.trdemoapi.exception.ConflictingStateException;
import com.example.trdemoapi.model.Course;
import com.example.trdemoapi.model.Subject;
import com.example.trdemoapi.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    private final TimetableService timetableService;

    public StudentController(CourseService courseService, UserService userService, GradeService gradeService, SubjectService subjectService, TimetableService timetableService) {
        this.courseService = courseService;
        this.userService = userService;
        this.gradeService = gradeService;
        this.subjectService = subjectService;
        this.timetableService = timetableService;
    }

    @Operation(summary="All available courses", description="Returns with all of the subjects available for the " +
            "authenticated user. A subject is available if the user is not registered for any of its courses.")
    @GetMapping("/available")
    public ResponseEntity<Page<Subject>> getAvailableSubjects(
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id,asc") String[] sortBy) {

        var currentUser = userService.loadCurrentUser();
        var subjects = subjectService.loadAvailableSubjectsForUser(currentUser, PageRequest.of(offset, pageSize,
                Sort.by(Sort.Order.by(sortBy[0]).with(Sort.Direction.fromString(sortBy[1])))));

        return ResponseEntity.ok().body(subjects);
    }

    @Operation(summary="All taken courses", description="Returns with all of the courses taken by the authenticated " +
            "user.")
    @GetMapping("/taken-courses")
    public ResponseEntity<Page<Course>> getTakenCourses(
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id,asc") String[] sortBy) {

        var currentUser = userService.loadCurrentUser();
        var courses = courseService.loadAllCoursesForStudent(currentUser, PageRequest.of(offset, pageSize,
                Sort.by(Sort.Order.by(sortBy[0]).with(Sort.Direction.fromString(sortBy[1])))));

        return ResponseEntity.ok().body(courses);
    }

    @Operation(summary="All taken courses in a timetable form")
    @GetMapping("/taken-courses/timetable")
    public ResponseEntity<TimetableResp> getTimetable() {
        var currentUser = userService.loadCurrentUser();
        var courses = courseService.loadAllCoursesForStudent(currentUser);
        var response = timetableService.generateTimetable(courses);

        return ResponseEntity.ok().body(new TimetableResp(response));
    }

    @Operation(summary="Register student for course", description="Returns with all of the courses taken by the " +
            "authenticated user.")
    @PostMapping("/taken-courses")
    public ResponseEntity<SuccessResp> takeCourse(@RequestBody Long courseId) {
        var currentUser = userService.loadCurrentUser();
        var course = courseService.loadCourseById(courseId);

        if (subjectService.isSubjectTakenByUser(currentUser, course.getSubject())) {
            throw new ConflictingStateException("Failed to register to course: another course is already taken from " +
                    "the same subject");
        }

        if (course.getRegisteredStudentCount() >= course.getCapacity()) {
            throw new ConflictingStateException("Failed to register to course: capacity is full");
        }

        if (courseService.isCourseConflictingWithTimetable(course, currentUser)) {
            throw new ConflictingStateException("Failed to register to course: conflicts in timetable");
        }

        courseService.registerUserOnCourse(currentUser, course);

        return ResponseEntity.ok().body(new SuccessResp(true, "User registered successfully"));
    }

    @Operation(summary="Unregister student from course", description="Returns with all of the courses taken by the " +
            "authenticated user.")
    @DeleteMapping("/taken-courses/{courseId}")
    public ResponseEntity<SuccessResp> dropCourse(@PathVariable Long courseId) {
        var currentUser = userService.loadCurrentUser();
        var course = courseService.loadCourseById(courseId);
        courseService.unregisterUserOnCourse(currentUser, course);

        return ResponseEntity.ok().body(new SuccessResp(true, "User unregistered successfully"));
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

    @Operation(summary="Get taken course of subject", description="If exists, returns with the course taken by the " +
            "user of the given subject.")
    @GetMapping("/subject/{subjectId}/get-taken-course")
    public ResponseEntity<Course> getTakenCourseOfSubject(@PathVariable Long subjectId) {
        var currentUser = userService.loadCurrentUser();
        var subject = subjectService.loadSubjectById(subjectId);

        return ResponseEntity.of(courseService.loadTakenCourseBySubjectAndStudent(subject, currentUser));
    }
}
