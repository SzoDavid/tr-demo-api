package com.example.trdemoapi.controller;

import com.example.trdemoapi.dto.SuccessResp;
import com.example.trdemoapi.model.Course;
import com.example.trdemoapi.model.Student;
import com.example.trdemoapi.dto.GradeReq;
import com.example.trdemoapi.service.CourseService;
import com.example.trdemoapi.service.GradeService;
import com.example.trdemoapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

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
    public ResponseEntity<Page<Course>> getAssignedCourses(
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id,asc") String[] sortBy) {

        var currentUser = userService.loadCurrentUser();
        var courses = courseService.loadAllCoursesForTeacher(currentUser,
                PageRequest.of(offset, pageSize,
                        Sort.by(Sort.Order.by(sortBy[0]).with(Sort.Direction.fromString(sortBy[1])))));
        return ResponseEntity.ok().body(courses);
    }

    @Operation(summary="Get assigned course by id", description="Returns with the details of the course with the given id.")
    @GetMapping("/courses/{id}")
    public ResponseEntity<Course> getAssignedCourseById(@PathVariable Long id) {
        var course = courseService.loadCourseById(id);

        var currentUser = userService.loadCurrentUser();

        if (!course.getTeacher().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }

        return ResponseEntity.ok().body(course);
    }

    @Operation(summary="Get students", description="Returns with all of the users registered to the given course.")
    @GetMapping("/courses/{courseId}/students")
    public ResponseEntity<Page<Student>> getStudents(
            @PathVariable Long courseId,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id,asc") String[] sortBy) {

        var course = courseService.loadCourseById(courseId);
        var currentUser = userService.loadCurrentUser();

        if (!course.getTeacher().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }

        var students = userService.loadUsersByCourse(course,
                PageRequest.of(offset, pageSize,
                        Sort.by(Sort.Order.by(sortBy[0]).with(Sort.Direction.fromString(sortBy[1])))));

        return ResponseEntity.ok().body(students);
    }

    @Operation(summary="Export students into csv")
    @GetMapping("/courses/{courseId}/students/export")
    public ResponseEntity<Resource> getStudentsCsv(@PathVariable Long courseId) {
        var course = courseService.loadCourseById(courseId);
        var currentUser = userService.loadCurrentUser();

        if (!course.getTeacher().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }

        var students = userService.loadAllUsersByCourse(course);
        var csvContent = generateCsv(students);

        var resource = new ByteArrayResource(csvContent.getBytes(StandardCharsets.UTF_8));

        var filename = "%s/%d-students.csv".formatted(course.getSubject().getName(),
                                                      course.getId());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename)
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }

    @Operation(summary="Grade students", description="Grade one or more students.")
    @PostMapping("/courses/{courseId}/grades")
    public ResponseEntity<SuccessResp> gradeStudents(@PathVariable Long courseId, @Valid @RequestBody GradeReq gradeRequest) {
        var course = courseService.loadCourseById(courseId);
        var currentUser = userService.loadCurrentUser();

        if (!course.getTeacher().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }

        for (var grade : gradeRequest.getGrades().entrySet()) {
            var user = userService.loadUserById(grade.getKey());
            gradeService.gradeStudent(course, user, grade.getValue());
        }

        return ResponseEntity.ok().body(new SuccessResp(true, "Grades saved successfully"));
    }

    @Operation(summary="Remove student from course")
    @DeleteMapping("/courses/{courseId}/students/{studentId}")
    public ResponseEntity<SuccessResp> removeStudentFromCourse(@PathVariable Long courseId, @PathVariable Long studentId) {
        var course = courseService.loadCourseById(courseId);
        var currentUser = userService.loadCurrentUser();

        if (!course.getTeacher().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }

        var student = userService.loadUserById(studentId);
        courseService.unregisterUserOnCourse(student, course);

        return ResponseEntity.ok().body(new SuccessResp(true, "Student removed successfully"));
    }

    private String generateCsv(List<Student> students) {
        var sb = new StringBuilder();
        sb.append("ID,Name,Grade\n");

        for (var student : students) {
            sb.append(student.user().getId()).append(",")
                    .append(student.user().getName()).append(",")
                    .append(student.grade() != null ? student.grade() : "").append("\n");
        }

        return sb.toString();
    }
}
