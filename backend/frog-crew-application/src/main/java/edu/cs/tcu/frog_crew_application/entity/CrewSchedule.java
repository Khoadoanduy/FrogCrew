package edu.cs.tcu.frog_crew_application.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "crew_schedules")
public class CrewSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "crew_schedule_id")
    private List<CrewAssignment> crewAssignments = new ArrayList<>();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public List<CrewAssignment> getCrewAssignments() {
        return crewAssignments;
    }

    public void setCrewAssignments(List<CrewAssignment> crewAssignments) {
        this.crewAssignments = crewAssignments;
    }

    public void addCrewAssignment(CrewAssignment crewAssignment) {
        crewAssignments.add(crewAssignment);
    }
}