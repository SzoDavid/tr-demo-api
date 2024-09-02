package com.example.trdemoapi.dto;

import com.example.trdemoapi.model.Course;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class TakenCourse {
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Course course;

    private final BigDecimal grade;

}
