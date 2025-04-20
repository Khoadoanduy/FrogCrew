// File: src/main/java/edu/cs/tcu/frog_crew_application/service/CrewAssignmentService.java
package edu.cs.tcu.frog_crew_application.service;

import edu.cs.tcu.frog_crew_application.dto.CrewAssignmentCreationDto;
import edu.cs.tcu.frog_crew_application.entity.CrewAssignment;
import edu.cs.tcu.frog_crew_application.entity.Game;
import edu.cs.tcu.frog_crew_application.entity.User;
import edu.cs.tcu.frog_crew_application.repository.CrewAssignmentRepository;
import edu.cs.tcu.frog_crew_application.repository.GameRepository;
import edu.cs.tcu.frog_crew_application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class CrewAssignmentService {

    private final CrewAssignmentRepository crewAssignmentRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    @Autowired
    public CrewAssignmentService(CrewAssignmentRepository crewAssignmentRepository,
                                  GameRepository gameRepository,
                                  UserRepository userRepository) {
        this.crewAssignmentRepository = crewAssignmentRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    public List<CrewAssignment> getCrewAssignmentsByGameId(Long gameId) {
        return crewAssignmentRepository.findByGameId(gameId);
    }
    

    public CrewAssignment saveAssignment(CrewAssignmentCreationDto creationDto) {
        Game game = gameRepository.findById(creationDto.getGameId())
                .orElseThrow(() -> new IllegalArgumentException("Game not found with ID: " + creationDto.getGameId()));

        User user = userRepository.findById(creationDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + creationDto.getUserId()));

        CrewAssignment crewAssignment = new CrewAssignment();
        crewAssignment.setGame(game);
        crewAssignment.setUser(user);
        crewAssignment.setPosition(creationDto.getPosition());
        crewAssignment.setReportLocation(creationDto.getReportLocation());
        crewAssignment.setReportTime(LocalTime.parse(creationDto.getReportTime()));

        return crewAssignmentRepository.save(crewAssignment);
    }
}
