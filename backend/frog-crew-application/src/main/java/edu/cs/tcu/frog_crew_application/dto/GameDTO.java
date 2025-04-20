package edu.cs.tcu.frog_crew_application.dto;

public class GameDTO {

    private String gameDate;
    private String gameTime;
    private String opponent;
    private String venue;
    private String requiredCrewPositions;

    // Constructor
    public GameDTO(String gameDate, String gameTime, String opponent, String venue, String requiredCrewPositions) {
        this.gameDate = gameDate;
        this.gameTime = gameTime;
        this.opponent = opponent;
        this.venue = venue;
        this.requiredCrewPositions = requiredCrewPositions;
    }

    // Getters
    public String getGameDate() {
        return gameDate;
    }

    public String getGameTime() {
        return gameTime;
    }

    public String getOpponent() {
        return opponent;
    }

    public String getVenue() {
        return venue;
    }

    public String getRequiredCrewPositions() {
        return requiredCrewPositions;
    }
}
