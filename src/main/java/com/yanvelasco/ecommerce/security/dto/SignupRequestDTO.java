package com.yanvelasco.ecommerce.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record SignupRequestDTO(

        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
        String password,

        Set<String> roles

) {
}
