package com.example.trdemoapi.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@ToString
public class PasswordChangeReq {
    @NotEmpty(message="Old password is required")
    private final String oldPassword;

    //TODO: add password pattern
    @NotEmpty(message="New password is required")
    private final String newPassword;
}
