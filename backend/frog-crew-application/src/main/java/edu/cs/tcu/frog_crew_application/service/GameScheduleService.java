package edu.cs.tcu.frog_crew_application.service;

import edu.cs.tcu.frog_crew_application.entity.Game;
import edu.cs.tcu.frog_crew_application.entity.GameSchedule;
import edu.cs.tcu.frog_crew_application.repository.GameRepository;
import edu.cs.tcu.frog_crew_application.repository.GameScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GameScheduleService {

    private final GameScheduleRepository gameScheduleRepository;
    private final GameRepository gameRepository;

    @Autowired
    public GameScheduleService(GameScheduleRepository gameScheduleRepository, GameRepository gameRepository) {
        this.gameScheduleRepository = gameScheduleRepository;
        this.gameRepository = gameRepository;
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @Transactional
    public GameSchedule createSchedule(GameSchedule gameSchedule) {
        return gameScheduleRepository.save(gameSchedule);
    }

    @Transactional
    public GameSchedule addGamesToSchedule(Long scheduleId, List<Game> games) {
        GameSchedule schedule = gameScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found with ID: " + scheduleId));
        
        for (Game game : games) {
            schedule.addGame(game);
        }
        
        return gameScheduleRepository.save(schedule);
    }
}