package com.example.trdemoapi.dto;

import com.example.trdemoapi.model.ERole;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@ToString
public class UpdateUserRolesReq {
    @NotEmpty(message="Roles are required")
    private final List<ERole> roles;
}
