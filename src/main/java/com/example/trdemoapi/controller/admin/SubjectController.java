package com.example.trdemoapi.controller.admin;

import com.example.trdemoapi.dto.CreateCourseReq;
import com.example.trdemoapi.dto.CreateSubjectReq;
import com.example.trdemoapi.dto.UpdateSubjectReq;
import com.example.trdemoapi.model.Subject;
import com.example.trdemoapi.service.CourseService;
import com.example.trdemoapi.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/admin/subjects")
@Tag(name="Subject", description="the endpoints for authenticated admins to manage subjects")
public class SubjectController {
    private final SubjectService subjectService;
    private final CourseService courseService;

    public SubjectController(SubjectService subjectService, CourseService courseService) {
        this.subjectService = subjectService;
        this.courseService = courseService;
    }

    @Operation(summary="All subjects", description="Returns with the details of all subjects.")
    @GetMapping("/")
    public ResponseEntity<Page<Subject>> getAllSubjects(@RequestParam(value = "offset", required = false) Integer offset,
                                                        @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                        @RequestParam(value = "sortBy", required = false) String sortBy) {
        if (offset == null) offset = 0;
        if (pageSize == null) pageSize = 10;
        if (StringUtils.isEmpty(sortBy)) sortBy ="id";

        var subjects = subjectService.getSubjectsPage(PageRequest.of(offset, pageSize, Sort.by(sortBy)));
        return ResponseEntity.ok().body(subjects);
    }

    @Operation(summary="Get subject by id", description="Returns with the details of the subject with the given id.")
    @GetMapping("/{id}")
    public ResponseEntity<Subject> getSubjectById(@PathVariable Long id) {
        var subject = subjectService.loadSubjectById(id);
        return ResponseEntity.ok().body(subject);
    }

    @Operation(summary="Create subject", description="Returns with the subject created.")
    @PostMapping("/")
    public ResponseEntity<Subject> createSubject(@Valid @RequestBody CreateSubjectReq request) {
        var subject = subjectService.createSubject(request);
        return ResponseEntity.ok().body(subject);
    }

    @Operation(summary="Update subject", description="Returns with the updated subject.")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubject(@PathVariable Long id, @Valid @RequestBody UpdateSubjectReq request) {
        var subject = subjectService.updateSubject(id, request);
        return ResponseEntity.ok().body(subject);
    }

    @Operation(summary="Delete subject", description="Deletes the subject with the given id.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubject(@PathVariable Long id) {
        var subject = subjectService.loadSubjectById(id);
        subjectService.deleteSubject(subject);

        return ResponseEntity.ok().body("Subject deleted successfully");
    }

    @Operation(summary="Add course", description="Creates a course for the given subject, and returns with the " +
            "subject. If the given user didn't had the teacher role yet, grants it.")
    @PostMapping("/{id}/courses")
    public ResponseEntity<Subject> addCourseToSubject(@PathVariable Long id, @Valid @RequestBody CreateCourseReq request) {
        var subject = subjectService.loadSubjectById(id);
        courseService.createCourse(subject, request);

        return ResponseEntity.ok().body(subject);
    }
}
