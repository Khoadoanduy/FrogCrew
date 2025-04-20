package edu.cs.tcu.frog_crew_application.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String crewMemberName;
    private Long gameId;
    private Boolean available;
    private String comment;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public String getCrewMemberName() {
        return crewMemberName;
    }

    public void setCrewMemberName(String crewMemberName) {
        this.crewMemberName = crewMemberName;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
