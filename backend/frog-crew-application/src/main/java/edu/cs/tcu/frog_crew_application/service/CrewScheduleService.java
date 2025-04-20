package edu.cs.tcu.frog_crew_application.service;

import edu.cs.tcu.frog_crew_application.entity.CrewSchedule;
import edu.cs.tcu.frog_crew_application.entity.Game;
import edu.cs.tcu.frog_crew_application.repository.CrewScheduleRepository;
import edu.cs.tcu.frog_crew_application.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CrewScheduleService {

    private final CrewScheduleRepository crewScheduleRepository;
    private final GameRepository gameRepository;

    @Autowired
    public CrewScheduleService(CrewScheduleRepository crewScheduleRepository, GameRepository gameRepository) {
        this.crewScheduleRepository = crewScheduleRepository;
        this.gameRepository = gameRepository;
    }

    @Transactional
    public CrewSchedule createCrewSchedule(Long gameId, CrewSchedule crewSchedule) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found with ID: " + gameId));

        crewSchedule.setGame(game);
        return crewScheduleRepository.save(crewSchedule);
    }
}