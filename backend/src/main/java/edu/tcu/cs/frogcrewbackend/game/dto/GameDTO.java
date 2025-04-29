package edu.tcu.cs.frogcrewbackend.game.dto;

import edu.tcu.cs.frogcrewbackend.game.GameSchedule;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record GameDTO (
        @NotNull(message = "Game id is required")
        Integer gameId,

        Integer scheduleId,

        @NotEmpty(message = "Game date is required")
        String gameDate,

        @NotEmpty(message = "Venue is required")
        String venue,

        @NotEmpty(message = "Opponent is required")
        String opponent,

        @NotNull(message = "Venue is required")
        Boolean isFinalized
) {
}
