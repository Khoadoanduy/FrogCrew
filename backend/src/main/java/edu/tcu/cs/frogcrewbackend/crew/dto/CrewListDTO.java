package edu.tcu.cs.frogcrewbackend.crew.dto;

import java.util.List;

public record CrewListDTO (
        Integer gameId,
        String gameDate,
        String venue,
        String opponent,
        List<CrewedUserDTO> crewedUserDTOS
){ }
