package edu.tcu.cs.frogcrewbackend.crew;

import edu.tcu.cs.frogcrewbackend.crew.converter.CrewedUserToCrewedUserDTOConverter;
import edu.tcu.cs.frogcrewbackend.crew.dto.CrewedUserDTO;
import edu.tcu.cs.frogcrewbackend.game.Game;
import edu.tcu.cs.frogcrewbackend.game.GameRepository;
import edu.tcu.cs.frogcrewbackend.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CrewedUserService {
    private final CrewedUserRepository crewedUserRepository;
    private final GameRepository gameRepository;

    public CrewedUserService(CrewedUserRepository crewedUserRepository, CrewedUserToCrewedUserDTOConverter crewedUserToCrewedUserDTOConverter, GameRepository gameRepository) {
        this.crewedUserRepository = crewedUserRepository;
        this.gameRepository = gameRepository;
    }

    // Find crewedUser by gameId and Position
    public List<CrewedUser> findCrewedUsersByGameAndPosition(Integer gameId, String position) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ObjectNotFoundException("crew user", gameId));

        List<CrewedUser> users = crewedUserRepository.findByGameAndPosition(game, position);

        return users;
    }
}
