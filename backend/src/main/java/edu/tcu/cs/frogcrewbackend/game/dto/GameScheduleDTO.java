package edu.tcu.cs.frogcrewbackend.game.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record GameScheduleDTO (
        @NotNull(message = "Schedule is required")
        Integer scheduleId,

        @NotEmpty(message = "Sport is required")
        String sport,

        @NotEmpty(message = "Season is required")
        String season
) { }
