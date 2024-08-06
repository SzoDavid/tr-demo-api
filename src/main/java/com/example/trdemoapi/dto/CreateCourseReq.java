package com.example.trdemoapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@ToString
public class CreateCourseReq {
    @NotNull(message="Capacity is required")
    @Min(value=0, message="Credit must be a positive number or zero")
    private int capacity;

    @NotNull(message="TeacherId is required")
    private long teacherId;
}
