package edu.cs.tcu.frog_crew_application.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

@Entity
public class CrewAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "game_id")
    private Game game; // Link to Game entity

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user; // Link to User entity

    @NotNull(message = "Position is required.")
    private String position;

    @NotNull(message = "Report time is required.")
    private LocalTime reportTime;

    @NotNull(message = "Report location is required.")
    private String reportLocation;

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LocalTime getReportTime() {
        return reportTime;
    }

    public void setReportTime(LocalTime reportTime) {
        this.reportTime = reportTime;
    }

    public String getReportLocation() {
        return reportLocation;
    }

    public void setReportLocation(String reportLocation) {
        this.reportLocation = reportLocation;
    }
}
