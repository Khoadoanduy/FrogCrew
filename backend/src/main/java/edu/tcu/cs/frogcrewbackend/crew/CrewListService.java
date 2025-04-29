package edu.tcu.cs.frogcrewbackend.crew;

import edu.tcu.cs.frogcrewbackend.crew.dto.CrewRequestDTO;
import edu.tcu.cs.frogcrewbackend.game.Game;
import edu.tcu.cs.frogcrewbackend.game.GameRepository;
import edu.tcu.cs.frogcrewbackend.member.UserRepository;
import jakarta.transaction.Transactional;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CrewListService {
    private final GameRepository gameRepository;
    private final CrewedUserRepository crewedUserRepository;
    private final CrewListRepository crewListRepository;
    private final UserRepository userRepository;

    public CrewListService(GameRepository gameRepository, CrewedUserRepository crewedUserRepository, CrewListRepository crewListRepository, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.crewedUserRepository = crewedUserRepository;
        this.crewListRepository = crewListRepository;
        this.userRepository = userRepository;
    }

    public CrewList addCrewListToGame(Integer gameId, List<CrewRequestDTO> crewRequestDTOList) {
        Game game = this.gameRepository.findById(gameId)
                .orElseThrow(() -> new ObjectNotFoundException("game", gameId));

        CrewList gameCrewList = new CrewList();

        List<CrewedUser> crewedUsers = crewRequestDTOList.stream()
                .map(dto -> {
                    CrewedUser cu = new CrewedUser();
                    cu.setCrewedUserId(dto.crewedUserId());
                    cu.setGame(game);
                    cu.setPosition(dto.position());
                    cu.setReportLocation(""); // Unsure where this comes from
                    cu.setReportTime(""); // Unsure where this comes from
                    cu.setUser(userRepository.findById(dto.userId())
                            .orElseThrow(() -> new ObjectNotFoundException("user", dto.userId())));
                    return this.crewedUserRepository.save(cu);
                })
                .toList();

        gameCrewList.setGame(game);
        gameCrewList.setCrewedUsers(crewedUsers);
        return this.crewListRepository.save(gameCrewList);
    }

    public CrewList findCrewListByGameId(Integer gameId) {
        Game crewedGame = this.gameRepository.findById(gameId)
                .orElseThrow(() -> new ObjectNotFoundException("game", gameId));

        CrewList gameCrew = crewedGame.getCrewList();
        return gameCrew;
    }

}
