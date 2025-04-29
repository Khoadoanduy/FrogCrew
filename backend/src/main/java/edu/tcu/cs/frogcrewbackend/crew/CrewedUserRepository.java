package edu.tcu.cs.frogcrewbackend.crew;

import edu.tcu.cs.frogcrewbackend.crew.dto.CrewedUserDTO;
import edu.tcu.cs.frogcrewbackend.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrewedUserRepository extends JpaRepository<CrewedUser, Integer> {
    List<CrewedUser> findByGameAndPosition(Game game, String position);

}
