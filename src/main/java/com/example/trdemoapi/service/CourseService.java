package com.example.trdemoapi.service;

import com.example.trdemoapi.dto.CreateCourseReq;
import com.example.trdemoapi.dto.UpdateCourseReq;
import com.example.trdemoapi.model.*;
import com.example.trdemoapi.repository.CourseRepository;
import com.example.trdemoapi.repository.StudentCourseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Transactional
public class CourseService {
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private final CourseRepository courseRepository;
    private final UserService userService;
    private final StudentCourseRepository studentCourseRepository;

    public CourseService(CourseRepository courseRepository, UserService userService, StudentCourseRepository studentCourseRepository) {
        this.courseRepository = courseRepository;
        this.userService = userService;
        this.studentCourseRepository = studentCourseRepository;
    }

    public Course loadCourseById(Long id) throws IllegalArgumentException {
        return courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Course not found."));
    }

    public Page<Course> loadAllCoursesForStudent(User student, PageRequest pageRequest) {
        return studentCourseRepository.findCoursesByStudentId(student.getId(), pageRequest);
    }

    public Page<Course> loadAllCoursesForTeacher(User teacher, PageRequest pageRequest) {
        return courseRepository.findCoursesByTeacherId(teacher.getId(), pageRequest);
    }

    public Optional<Course> loadTakenCourseBySubjectAndStudent(Subject subject, User student) {
        return studentCourseRepository.findCourseByStudentIdAndSubjectId(student.getId(), subject.getId());
    }

    @Transactional
    public Course createCourse(Subject subject, CreateCourseReq request) {
        var teacher = userService.loadUserById(request.getTeacherId());

        var course = new Course()
                .setCapacity(request.getCapacity())
                .setSubject(subject)
                .setTeacher(teacher)
                .setDay(request.getSchedule().getDay())
                .setStartTime(LocalTime.parse(request.getSchedule().getStartTime(), timeFormatter))
                .setEndTime(LocalTime.parse(request.getSchedule().getEndTime(), timeFormatter));

        userService.addUserRole(teacher, ERole.TEACHER);
        return courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(Course course) {
        courseRepository.delete(course);
    }

    @Transactional
    public Course updateCourse(Long id, UpdateCourseReq request) throws IllegalArgumentException {
        var course = courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Course not found."));

        if (request.getCapacity() != null) course.setCapacity(request.getCapacity());
        if (request.getTeacherId() != null) {
            var teacher = userService.loadUserById(request.getTeacherId());
            course.setTeacher(teacher);
            userService.addUserRole(teacher, ERole.TEACHER);
        }
        if (request.getSchedule() != null) {
            course.setDay(request.getSchedule().getDay())
                  .setStartTime(LocalTime.parse(request.getSchedule().getStartTime(), timeFormatter))
                  .setEndTime(LocalTime.parse(request.getSchedule().getEndTime(), timeFormatter));
        }

        return courseRepository.save(course);
    }

    @Transactional
    public void registerUserOnCourse(User user, Course course) throws IllegalArgumentException {
        if (!user.hasRole(ERole.STUDENT)) throw new IllegalArgumentException("User is not a student.");

        var id = new StudentCourseId()
                .setStudentId(user.getId())
                .setCourseId(course.getId());

        var registration = new StudentCourse()
                .setId(id)
                .setStudent(user)
                .setCourse(course);

        studentCourseRepository.save(registration);
    }

    public void unregisterUserOnCourse(User user, Course course) throws IllegalArgumentException {
        var registration = studentCourseRepository.findStudentCoursesById(new StudentCourseId(user.getId(), course.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Registration not found."));
        studentCourseRepository.delete(registration);
    }
}
