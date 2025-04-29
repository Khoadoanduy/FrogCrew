package edu.tcu.cs.frogcrewbackend.availability.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AvailabilityDTO(
        @NotNull(message = "User id is required")
        Integer userId,

        @NotNull(message = "Game id is required")
        Integer gameId,

        @NotNull(message = "Availability is required")
        Boolean availability,

        String comment
) {}
