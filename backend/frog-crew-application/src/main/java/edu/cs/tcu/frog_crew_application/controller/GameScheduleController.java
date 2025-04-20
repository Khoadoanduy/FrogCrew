package edu.cs.tcu.frog_crew_application.controller;

import edu.cs.tcu.frog_crew_application.entity.Game;
import edu.cs.tcu.frog_crew_application.entity.GameSchedule;
import edu.cs.tcu.frog_crew_application.service.GameScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gameSchedule")
public class GameScheduleController {

    private final GameScheduleService gameScheduleService;

    @Autowired
    public GameScheduleController(GameScheduleService gameScheduleService) {
        this.gameScheduleService = gameScheduleService;
    }

    @GetMapping("/games")
    public ResponseEntity<List<Game>> getAllGames() {
        List<Game> games = gameScheduleService.getAllGames();
        return ResponseEntity.ok(games);
    }

    @PostMapping
    public ResponseEntity<GameSchedule> createSchedule(@RequestBody GameSchedule gameSchedule) {
        GameSchedule createdSchedule = gameScheduleService.createSchedule(gameSchedule);
        return ResponseEntity.ok(createdSchedule);
    }

    @PostMapping("/{scheduleId}/games")
    public ResponseEntity<GameSchedule> addGamesToSchedule(
            @PathVariable Long scheduleId,
            @RequestBody List<Game> games) {
        GameSchedule updatedSchedule = gameScheduleService.addGamesToSchedule(scheduleId, games);
        return ResponseEntity.ok(updatedSchedule);
    }
}