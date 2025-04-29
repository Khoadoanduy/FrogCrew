package edu.tcu.cs.frogcrewbackend.crew;

import edu.tcu.cs.frogcrewbackend.crew.dto.CrewedUserDTO;
import edu.tcu.cs.frogcrewbackend.game.Game;
import edu.tcu.cs.frogcrewbackend.game.GameRepository;
import edu.tcu.cs.frogcrewbackend.system.StatusCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureMockMvc
public class CrewedUserServiceTest {
    @Mock
    CrewedUserRepository crewedUserRepository;

    @Mock
    GameRepository gameRepository;

    @InjectMocks
    CrewedUserService crewedUserService;

    @BeforeEach
    void setup(){}

    @AfterEach
    void teardown(){}

    @Test
    void testFindCrewedUsersByGameAndPositionSuccess() {
        // Given
        Game game = new Game();
        game.setGameId(1);

        CrewedUser cu1 = new CrewedUser();
        cu1.setCrewedUserId(1);
        cu1.setGame(game);
        cu1.setPosition("PRODUCER");

        CrewedUser cu2 = new CrewedUser();
        cu2.setCrewedUserId(2);
        cu2.setGame(game);
        cu2.setPosition("PRODUCER");

        List<CrewedUser> list = List.of(cu1, cu2);

        given(gameRepository.findById(1)).willReturn(Optional.of(game));
        given(crewedUserRepository.findByGameAndPosition(game, "PRODUCER")).willReturn(list);
        // When
        List<CrewedUser> result = crewedUserService.findCrewedUsersByGameAndPosition(1, "PRODUCER");

        // Then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getPosition()).isEqualTo("PRODUCER");
        verify(crewedUserRepository, times(1)).findByGameAndPosition(game, "PRODUCER");
    }



}
