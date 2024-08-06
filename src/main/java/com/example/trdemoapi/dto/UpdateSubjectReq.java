package com.example.trdemoapi.dto;

import com.example.trdemoapi.model.SubjectType;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@ToString
public class UpdateSubjectReq {
    @NotBlank(message="Name cannot be blank")
    private String name;

    private SubjectType type;

    @Min(value=0, message="Credit must be a positive number or zero")
    private Integer credit;
}
