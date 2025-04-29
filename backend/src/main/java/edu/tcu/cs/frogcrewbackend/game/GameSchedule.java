package edu.tcu.cs.frogcrewbackend.game;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Entity
public class GameSchedule implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer scheduleId;

    @NotEmpty(message =  "sport required")
    private String sport;

    @NotEmpty(message =  "season required")
    private String season;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "schedule")
    private List<Game> games = new ArrayList<>();

    public Integer getId() {
        return scheduleId;
    }

    public void setId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public void addGame(Game game) {
        game.setSchedule(this);
        this.games.add(game);
    }
}
