package com.example.trdemoapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
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

    @NotEmpty(message="New password is required")
    @Pattern(regexp="^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$",
            message="The password must be at least 8 characters long, containing at least one uppercase letter, one " +
                    "lowercase letter, one digit, and may include the special characters `@$!%*?&`")
    private final String newPassword;
}
