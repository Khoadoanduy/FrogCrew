package edu.cs.tcu.frog_crew_application.service;

import edu.cs.tcu.frog_crew_application.dto.GameDTO;
import edu.cs.tcu.frog_crew_application.entity.Game;
import edu.cs.tcu.frog_crew_application.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<GameDTO> getAllGames() {
        List<Game> games = gameRepository.findAll();

        return games.stream()
                .map(game -> new GameDTO(
                        game.getGameDate().toString(),   // Convert LocalDate to String
                        game.getGameTime().toString(),   // Convert LocalTime to String
                        game.getOpponent(),
                        game.getVenue(),
                        game.getRequiredCrewPositions()
                ))
                .collect(Collectors.toList());
    }

    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }
    
    public List<Game> saveGames(List<Game> games) {
        return gameRepository.saveAll(games);
    }    
}
