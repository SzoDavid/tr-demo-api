package com.example.trdemoapi.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@ToString
public final class LoginReq {
    @NotEmpty(message="Email is required")
    private final String email;

    @NotEmpty(message="Password is required")
    private final String password;
}
