package edu.tcu.cs.frogcrewbackend.game;

import jakarta.transaction.Transactional;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class GameScheduleService {
    private final GameScheduleRepository gameScheduleRepository;
    private final GameRepository gameRepository;

    public GameScheduleService(GameScheduleRepository gameRepository, GameRepository gameRepository1) {
        this.gameScheduleRepository = gameRepository;
        this.gameRepository = gameRepository1;
    }

    public Game addGame(Game newGame){
        return this.gameRepository.save(newGame);
    }

    public GameSchedule addGameSchedule(GameSchedule gameSchedule) {
        return this.gameScheduleRepository.save(gameSchedule);
    }

    public List<GameSchedule> findAllGameSchedules() {
        return this.gameScheduleRepository.findAll();
    }

    public List<Game> findAllGames() {
        return this.gameRepository.findAll();
    }

    public void addGameToSchedule(Integer scheduleId, Game game) {
        GameSchedule assignedSchedule = this.gameScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ObjectNotFoundException("game schedule", scheduleId));

        assignedSchedule.addGame(game);
        game.setSchedule(assignedSchedule);

        this.gameRepository.save(game);
    }

    public List<Game> findGamesByScheduleId(int scheduleId) {
        GameSchedule schedule = this.gameScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ObjectNotFoundException("game", scheduleId));
        List<Game> scheduledGames = schedule.getGames();
        return scheduledGames;
    }

}
