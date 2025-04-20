package edu.cs.tcu.frog_crew_application.dto;

public class CrewAssignmentCreationDto {
    private Long gameId;
    private Long userId;
    private String position;
    private String reportTime;
    private String reportLocation;

    // Empty constructor (important for Spring to deserialize)
    public CrewAssignmentCreationDto() {
    }

    // Getters and Setters
    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getReportLocation() {
        return reportLocation;
    }

    public void setReportLocation(String reportLocation) {
        this.reportLocation = reportLocation;
    }
}
