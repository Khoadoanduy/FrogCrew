package edu.tcu.cs.frogcrewbackend.crew;

import edu.tcu.cs.frogcrewbackend.crew.dto.CrewRequestDTO;
import edu.tcu.cs.frogcrewbackend.game.Game;
import edu.tcu.cs.frogcrewbackend.game.GameRepository;
import edu.tcu.cs.frogcrewbackend.game.GameSchedule;
import edu.tcu.cs.frogcrewbackend.member.Member;
import edu.tcu.cs.frogcrewbackend.member.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureMockMvc
public class CrewListServiceTest {
    @Mock
    CrewListRepository crewListRepository;

    @Mock
    GameRepository gameRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    CrewedUserRepository crewedUserRepository;

    @InjectMocks
    CrewListService crewListService;

    @BeforeEach
    void setup(){}

    @AfterEach
    void tearDown(){}

    @Test
    void testAddCrewListToGame() {
        // Given
        Game game = new Game();
        game.setGameId(1);

        Member member = new Member();
        member.setId(2);
        member.setFirstName("Clark");
        member.setLastName("Kent");

        CrewRequestDTO crewRequestDTO = new CrewRequestDTO(1, 2, 1, "CAMERA");

        CrewedUser crewedUser = new CrewedUser();
        crewedUser.setCrewedUserId(1);
        crewedUser.setGame(game);
        crewedUser.setPosition("CAMERA");
        crewedUser.setReportTime("");
        crewedUser.setReportLocation("");
        crewedUser.setUser(member);

        CrewList expectedCrewList = new CrewList();
        expectedCrewList.setGame(game);
        expectedCrewList.setCrewedUsers(List.of(crewedUser));

        given(gameRepository.findById(1)).willReturn(Optional.of(game));
        given(userRepository.findById(2)).willReturn(Optional.of(member));
        given(crewedUserRepository.save(any(CrewedUser.class))).willReturn(crewedUser);
        given(crewListRepository.save(any(CrewList.class))).willReturn(expectedCrewList);

        // When
        CrewList actualCrewList = crewListService.addCrewListToGame(1, List.of(crewRequestDTO));

        // Then
        assertThat(actualCrewList.getGame().getGameId()).isEqualTo(1);
        assertThat(actualCrewList.getCrewedUsers()).hasSize(1);
        assertThat(actualCrewList.getCrewedUsers().get(0).getPosition()).isEqualTo("CAMERA");

        verify(gameRepository, times(1)).findById(1);
        verify(crewListRepository, times(1)).save(any(CrewList.class));
    }

    @Test
    void testFindCrewListByGameId() {
        // Given
        Game game1 = new Game();
        game1.setGameId(1);

        Member mem1 = new Member();
        mem1.setId(2);
        mem1.setFirstName("Clark");
        mem1.setLastName("Kent");

        CrewedUser cu1 = new CrewedUser();
        cu1.setCrewedUserId(1);
        cu1.setGame(game1);
        cu1.setUser(mem1);

        CrewList crewList = new CrewList();
        crewList.setGame(game1);
        crewList.setCrewedUsers(List.of(cu1));

        game1.setCrewList(crewList);

        given(this.gameRepository.findById(1)).willReturn(java.util.Optional.of(game1));
        given(crewListRepository.save(any(CrewList.class))).willReturn(crewList);

        // When
        CrewList cl = this.crewListService.findCrewListByGameId(1);

        // Then
        assertThat(cl.getGame()).isEqualTo(game1);
        assertThat(cl.getCrewedUsers()).isEqualTo(List.of(cu1));
        verify(gameRepository, times(1)).findById(1);
    }
}
