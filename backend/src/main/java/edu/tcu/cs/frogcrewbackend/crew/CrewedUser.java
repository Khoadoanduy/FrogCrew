package edu.tcu.cs.frogcrewbackend.crew;

import edu.tcu.cs.frogcrewbackend.game.Game;
import edu.tcu.cs.frogcrewbackend.member.Member;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@Transactional
@Entity
public class CrewedUser implements Serializable {

    @Id
    private Integer crewedUserId;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Member user;

    @ManyToOne
    @JoinColumn(name = "gameId", nullable = false)
    private Game game;

    @NotEmpty(message = "position required")
    private String position;

    private String reportTime;

    private String reportLocation;

    // Getters and Setters
    public Integer getCrewedUserId() {
        return crewedUserId;
    }

    public void setCrewedUserId(Integer id) {
        this.crewedUserId = id;
    }

    public Member getUser() {
        return user;
    }

    public void setUser(Member user) {
        this.user = user;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
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
