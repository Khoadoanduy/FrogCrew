package edu.tcu.cs.frogcrewbackend.crew.dto;

public record CrewedUserDTO(
        Integer crewedUserId,
        Integer userId,
        Integer gameId,
        String fullName,
        String position,
        String reportTime,
        String reportLocation
) {}
