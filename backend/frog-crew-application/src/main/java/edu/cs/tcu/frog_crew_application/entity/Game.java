package edu.cs.tcu.frog_crew_application.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Sport type is required.")
    private String sportType;

    @NotNull(message = "Game date is required.")
    private LocalDate gameDate;

    @NotNull(message = "Game time is required.")
    private LocalTime gameTime;

    private String opponent; // Optional

    @NotBlank(message = "Venue is required.")
    private String venue;

    @NotBlank(message = "Required crew positions are required.")
    private String requiredCrewPositions;

    private String status = "DRAFT"; // Default draft mode

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public String getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    public LocalDate getGameDate() {
        return gameDate;
    }

    public void setGameDate(LocalDate gameDate) {
        this.gameDate = gameDate;
    }

    public LocalTime getGameTime() {
        return gameTime;
    }

    public void setGameTime(LocalTime gameTime) {
        this.gameTime = gameTime;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getRequiredCrewPositions() {
        return requiredCrewPositions;
    }

    public void setRequiredCrewPositions(String requiredCrewPositions) {
        this.requiredCrewPositions = requiredCrewPositions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
