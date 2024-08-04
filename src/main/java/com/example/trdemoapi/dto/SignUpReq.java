package com.example.trdemoapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@ToString
public final class SignUpReq {
    @NotEmpty(message="Name is required")
    @Size(min=2, max=255, message="The length of name must be between 2 and 255")
    private final String name;

    @NotEmpty(message="Email is required")
    @Email(message="The email address is invalid.", flags={Pattern.Flag.CASE_INSENSITIVE})
    private final String email;

    @NotEmpty(message="Password is required")
    private final String password;
}
