package edu.cs.tcu.frog_crew_application.dto;

public class AvailabilityDTO {

    private String crewMemberName;
    private Long gameId;
    private Boolean available;
    private String comment;

    // Constructor
    public AvailabilityDTO(String crewMemberName, Long gameId, Boolean available, String comment) {
        this.crewMemberName = crewMemberName;
        this.gameId = gameId;
        this.available = available;
        this.comment = comment;
    }

    // Getters
    public String getCrewMemberName() {
        return crewMemberName;
    }

    public Long getGameId() {
        return gameId;
    }

    public Boolean getAvailable() {
        return available;
    }

    public String getComment() {
        return comment;
    }
}
