package com.example.trdemoapi.service;

import com.example.trdemoapi.dto.CreateCourseReq;
import com.example.trdemoapi.dto.UpdateCourseReq;
import com.example.trdemoapi.model.Course;
import com.example.trdemoapi.model.ERole;
import com.example.trdemoapi.model.Subject;
import com.example.trdemoapi.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserService userService;

    public CourseService(CourseRepository courseRepository, UserService userService) {
        this.courseRepository = courseRepository;
        this.userService = userService;
    }

    public Course loadCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Course not found."));
    }

    @Transactional
    public Course createCourse(Subject subject, CreateCourseReq request) {
        var teacher = userService.loadUserById(request.getTeacherId());
        var course = new Course()
                .setCapacity(request.getCapacity())
                .setSubject(subject)
                .setTeacher(teacher);

        userService.addUserRole(teacher, ERole.TEACHER);
        return courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(Course course) {
        courseRepository.delete(course);
    }

    @Transactional
    public Course updateCourse(Long id, UpdateCourseReq request) {
        var course = courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Course not found."));

        if (request.getCapacity() != null) course.setCapacity(request.getCapacity());
        if (request.getTeacherId() != null) {
            var teacher = userService.loadUserById(request.getTeacherId());
            course.setTeacher(teacher);
            userService.addUserRole(teacher, ERole.TEACHER);
        }

        return courseRepository.save(course);
    }
}
