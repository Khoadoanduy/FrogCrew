package edu.cs.tcu.frog_crew_application.controller;

import edu.cs.tcu.frog_crew_application.dto.GameDTO;
import edu.cs.tcu.frog_crew_application.entity.Game;
import edu.cs.tcu.frog_crew_application.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<?> getAllGames() {
        List<GameDTO> games = gameService.getAllGames();
        if (games.isEmpty()) {
            return ResponseEntity.ok("No upcoming games available at this time."); // 3a: No games scheduled
        } else {
            return ResponseEntity.ok(games);
        }
    }

    @PostMapping
    public ResponseEntity<Game> createGame(@RequestBody Game game) {
        Game savedGame = gameService.saveGame(game);
        return ResponseEntity.ok(savedGame);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Game>> createGames(@RequestBody List<Game> games) {
        List<Game> savedGames = gameService.saveGames(games);
        return ResponseEntity.ok(savedGames);
    }

}
