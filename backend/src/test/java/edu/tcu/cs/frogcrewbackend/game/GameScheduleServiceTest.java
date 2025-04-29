package edu.tcu.cs.frogcrewbackend.game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureMockMvc
public class GameScheduleServiceTest {
    private static final Logger log = LoggerFactory.getLogger(GameScheduleServiceTest.class);

    @Mock
    GameScheduleRepository gameScheduleRepository;

    @Mock
    GameRepository gameRepository;

    @InjectMocks
    GameScheduleService gameScheduleService;

    List<GameSchedule> schedules;

    @BeforeEach
    void setUp() {
        GameSchedule schedule1 = new GameSchedule();
        schedule1.setId(1);
        schedule1.setSport("Baseball");
        schedule1.setSeason("2024-2025");

        GameSchedule schedule2 = new GameSchedule();
        schedule2.setId(2);
        schedule2.setSport("Football");
        schedule2.setSeason("2023-2024");

        this.schedules = new ArrayList<>();
        schedules.add(schedule1);
        schedules.add(schedule2);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void testAddGameScheduleSuccess(){
        // Given
        GameSchedule gameSchedule = new GameSchedule();
        gameSchedule.setId(1);
        gameSchedule.setSport("Baseball");
        gameSchedule.setSeason("2024-2025");

        given(this.gameScheduleRepository.save(gameSchedule)).willReturn(gameSchedule);

        // When
        GameSchedule returnedSchedule = this.gameScheduleService.addGameSchedule(gameSchedule);

        // Then
        assertThat(returnedSchedule.getId()).isEqualTo(gameSchedule.getId());
        assertThat(returnedSchedule.getSport()).isEqualTo(gameSchedule.getSport());
        assertThat(returnedSchedule.getSeason()).isEqualTo(gameSchedule.getSeason());
        verify(this.gameScheduleRepository, times(1)).save(gameSchedule);
    }

    @Test
    void testFindAllGameSchedulesSuccess() {
        // Given
        given(this.gameScheduleRepository.findAll()).willReturn(this.schedules);

        // When
        List<GameSchedule> returnedSchedules = this.gameScheduleService.findAllGameSchedules();

        // Then
        assertThat(returnedSchedules.size()).isEqualTo(this.schedules.size());
        verify(this.gameScheduleRepository, times(1)).findAll();
    }

    @Test
    void testAddGameToScheduleSuccess() {
        // Given
        GameSchedule schedule = new GameSchedule();
        schedule.setId(2);
        schedule.setSport("Football");
        schedule.setSeason("2023-2024");

        Game newGame = new Game();
        newGame.setGameId(10);
        newGame.setGameDate("2024-01-01");
        newGame.setVenue("Carter");
        newGame.setOpponent("SMU");
        newGame.setFinalized(true);

        given(this.gameScheduleRepository.findById(2)).willReturn(java.util.Optional.of(schedule));
        given(this.gameRepository.save(newGame)).willReturn(newGame);

        // When
        this.gameScheduleService.addGameToSchedule(2, newGame);

        // Then
        assertThat(newGame.getSchedule().getId()).isEqualTo(2);
        assertThat(schedule.getGames().contains(newGame));
    }

    @Test
    void testFindGamesByScheduleIdSuccess() {
        // Given
        int scheduleId = 1;

        Game game1 = new Game();
        game1.setGameId(1);
        game1.setGameDate("2025-02-02");
        game1.setVenue("Carter");
        game1.setOpponent("Baylor");
        game1.setFinalized(true);

        Game game2 = new Game();
        game2.setGameId(2);
        game2.setGameDate("2025-02-09");
        game2.setVenue("Globe Life");
        game2.setOpponent("UT");
        game2.setFinalized(false);

        GameSchedule schedule = new GameSchedule();
        schedule.setId(scheduleId);
        schedule.setSport("Baseball");
        schedule.setSeason("2024-2025");
        schedule.getGames().add(game1);
        schedule.getGames().add(game2);

        given(this.gameScheduleRepository.findById(scheduleId)).willReturn(java.util.Optional.of(schedule));

        // When
        List<Game> games = this.gameScheduleService.findGamesByScheduleId(scheduleId);

        // Then
        assertThat(games.size()).isEqualTo(2);
        assertThat(games).contains(game1, game2);
        verify(this.gameScheduleRepository, times(1)).findById(scheduleId);
    }


}
