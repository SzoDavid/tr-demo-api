package com.example.trdemoapi.dto;

import com.example.trdemoapi.model.SubjectType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@ToString
public class CreateSubjectReq {
    @NotEmpty(message="Name is required")
    private String name;

    @NotNull(message="Type is required")
    private SubjectType type;

    @NotNull(message="Credit is required")
    @Min(value=0, message="Credit must be a positive number or zero")
    private int credit;
}
