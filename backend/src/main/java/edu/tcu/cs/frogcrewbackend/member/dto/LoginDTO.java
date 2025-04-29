package edu.tcu.cs.frogcrewbackend.member.dto;

public record LoginDTO (
        Integer userId,
        String role,
        String token
){
}
