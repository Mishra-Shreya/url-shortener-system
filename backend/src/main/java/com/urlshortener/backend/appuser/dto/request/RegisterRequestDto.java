package com.urlshortener.backend.appuser.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDto {

    @NotBlank(message = "Name is mandatory.")
    private String name;

    @NotBlank(message = "Email is mandatory.")
    @Email(message = "Invalid email address")
    private String email;

    @NotBlank(message = "Role is mandatory.")
    @Pattern(
            regexp = "^(user|admin|USER|ADMIN)$",
            message = "Invalid role"
    )
    private String role;

    @NotBlank(message = "User id is mandatory.")
    @Size(min = 5, max = 20, message = "User id must be between 5 and 20 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])[a-z0-9_-]{5,20}$",
            message = "Invalid User id. User id can contain only letters, numbers, underscore, hyphen"
    )
    private String userId;

    @NotBlank(message = "Password is mandatory.")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[_@#$-])[A-Za-z0-9_@#$-]{8,20}$",
            message = "Invalid password. Password can contain only letters, numbers, underscore, hyphen, @, # and $"
    )
    private String password;

}
