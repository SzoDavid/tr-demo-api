package com.example.trdemoapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@ToString
public class UpdateCourseReq {
    @Min(value=0, message="Credit must be a positive number or zero")
    private Integer capacity;

    private Long teacherId;

    @Valid
    private Schedule schedule;
}
