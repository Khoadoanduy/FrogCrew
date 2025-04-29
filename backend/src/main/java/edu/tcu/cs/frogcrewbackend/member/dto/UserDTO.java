package edu.tcu.cs.frogcrewbackend.member.dto;

import jakarta.validation.constraints.*;

public record UserDTO(
        Integer id,

        @NotEmpty(message = "First name is required")
        String firstName,

        @NotEmpty(message = "Last name is required")
        String lastName,

        @NotEmpty(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @NotEmpty(message = "Phone number is required")
        @Pattern(
                regexp = "^\\d{10}$",
                message = "Phone number must be valid"
        )
        String phoneNumber,

        @NotEmpty(message = "Role is required")
        String role,

        @NotEmpty(message = "Positions")
        String positions
) {}
