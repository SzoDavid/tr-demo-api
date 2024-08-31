package com.example.trdemoapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Formula;

import java.time.LocalTime;

@Entity
@Table(name = "courses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('courses_id_seq'::regclass)")
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "courses"})
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teacher_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User teacher;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @NotNull
    @Column(name = "day", nullable = false)
    private Short day;

    @NotNull
    @Column(name = "start_time", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Schema(description = "Start time of the course in HH:mm format", example = "12:00")
    private LocalTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Schema(description = "End time of the course in HH:mm format", example = "12:45")
    private LocalTime endTime;

    @Formula("(SELECT COUNT(sc.student_id) FROM student_courses sc WHERE sc.course_id = id)")
    private Integer registeredStudentCount;
}