package com.example.trdemoapi.service;

import com.example.trdemoapi.dto.TakenCourse;
import com.example.trdemoapi.dto.timetable.TimetableElement;
import com.example.trdemoapi.model.Course;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TimetableService {
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public Map<Short, List<TimetableElement>> generateTimetable(List<Course> courses) {
        return courses.stream()
                .collect(Collectors.groupingBy(
                        Course::getDay,
                        Collectors.collectingAndThen(
                                Collectors.mapping(
                                        course -> new TimetableElement(
                                                course.getSubject().getId(),
                                                course.getSubject().getName(),
                                                course.getStartTime().format(timeFormatter),
                                                course.getEndTime().format(timeFormatter)
                                        ),
                                        Collectors.toList()
                                ),
                                elements -> elements.stream()
                                        .sorted(Comparator.comparing(TimetableElement::startTime))
                                        .collect(Collectors.toList())
                        )
                ));
    }
}
