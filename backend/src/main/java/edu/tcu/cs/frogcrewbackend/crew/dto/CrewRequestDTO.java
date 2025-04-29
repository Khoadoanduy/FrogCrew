package edu.tcu.cs.frogcrewbackend.crew.dto;

public record CrewRequestDTO (
        Integer crewedUserId,
        Integer userId,
        Integer gameId,
        String position
){
}
