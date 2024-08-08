package com.example.trdemoapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@ToString
public class GradeReq {
    @NotEmpty(message="Grades are required")
    private Map<@NotNull(message="UserId is required")
                Long,
                @NotNull(message="Grade is required")
                @Range(min=1, max=5, message="Grade must be between 1 and 5")
                Integer> grades;
}
