package com.example.trdemoapi.dto;

import com.example.trdemoapi.model.ERole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@ToString
public class CreateUserReq {
    @NotEmpty(message="Name is required")
    @Size(min=2, max=255, message="The length of name must be between 2 and 255")
    private final String name;

    @NotEmpty(message="Email is required")
    @Email(message="The email address is invalid.", flags={Pattern.Flag.CASE_INSENSITIVE})
    private final String email;

    @NotEmpty(message="Password is required")
    @Pattern(regexp="^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$",
            message="The password must be at least 8 characters long, containing at least one uppercase letter, one " +
                    "lowercase letter, one digit, and may include the special characters `@$!%*?&`")
    private final String password;

    @NotEmpty(message="Roles are required")
    private final List<ERole> roles;
}
