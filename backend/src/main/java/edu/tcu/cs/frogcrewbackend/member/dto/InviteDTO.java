package edu.tcu.cs.frogcrewbackend.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record InviteDTO(
        @NotEmpty(message = "At least one email must be provided.")
        List<@Email(message = "Invalid email address.") String> emails
) {}
