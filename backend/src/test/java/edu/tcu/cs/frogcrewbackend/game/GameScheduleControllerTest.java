package edu.tcu.cs.frogcrewbackend.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.frogcrewbackend.availability.converter.AvailabilityDTOToAvailabilityConverter;
import edu.tcu.cs.frogcrewbackend.game.converter.GameToGameDTOConverter;
import edu.tcu.cs.frogcrewbackend.game.converter.ScheduleToScheduleDTOConverter;
import edu.tcu.cs.frogcrewbackend.game.dto.GameDTO;
import edu.tcu.cs.frogcrewbackend.game.dto.GameScheduleDTO;
import edu.tcu.cs.frogcrewbackend.system.StatusCode;
import org.hibernate.query.sqm.mutation.internal.cte.CteInsertStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class GameScheduleControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    GameScheduleService gameScheduleService;

    @MockitoBean
    ScheduleToScheduleDTOConverter scheduleToScheduleDTOConverter;

    @MockitoBean
    GameToGameDTOConverter gameToGameDTOConverter;

    List<GameSchedule> schedules;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setup() throws Exception {
        Game game1 = new Game();
        game1.setGameId(1);
        game1.setGameDate("2025-02-02");
        game1.setVenue("Carter");
        game1.setOpponent("Baylor");
        game1.setFinalized(true);

        Game game2 = new Game();
        game2.setGameId(1);
        game2.setGameDate("2023-02-02");
        game2.setVenue("Kyle");
        game2.setOpponent("TAMU");
        game2.setFinalized(true);

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

    @Test
    void testAddGameScheduleSuccess() throws Exception {
        GameScheduleDTO scheduleDTO = new GameScheduleDTO(1, "BaseBall", "2024-2025");

        GameSchedule schedule1 = new GameSchedule();
        schedule1.setId(1);
        schedule1.setSport("Baseball");
        schedule1.setSeason("2024-2025");

        String json = this.objectMapper.writeValueAsString(scheduleDTO);

        // Given
        given(this.gameScheduleService.addGameSchedule(Mockito.any(GameSchedule.class))).willReturn(schedule1);
        given(this.scheduleToScheduleDTOConverter.convert(schedule1)).willReturn(scheduleDTO);

        // When and Then
        this.mockMvc.perform(
                        post(this.baseUrl + "/gameSchedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Schedule added"))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @Test
    void testFindAllGameSchedulesSuccess() throws Exception {
        // Given
        given(this.gameScheduleService.findAllGameSchedules()).willReturn(this.schedules);

        // When and Then
        this.mockMvc.perform(get(this.baseUrl + "/gameSchedule")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Found all schedules"))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @Test
    void testAddGameToScheduleSuccess() throws Exception {
        Game game1 = new Game();
        game1.setGameId(1);
        game1.setGameDate("2025-02-02");
        game1.setVenue("Carter");
        game1.setOpponent("Baylor");
        game1.setFinalized(true);

        GameSchedule schedule1 = new GameSchedule();
        schedule1.setId(1);
        schedule1.setSport("Baseball");
        schedule1.setSeason("2024-2025");

        GameDTO gameDTO = new GameDTO(1, 1, "2025-02-02", "Carter", "Baylor", Boolean.TRUE);

        String json = this.objectMapper.writeValueAsString(game1);

        // Given
        doNothing().when(this.gameScheduleService).addGameToSchedule(1, game1);
        given(this.gameToGameDTOConverter.convert(game1)).willReturn(gameDTO);

        // When and Then
        this.mockMvc.perform(post(this.baseUrl + "/gameSchedule/1/game")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Game added to schedule 1"));
    }

    @Test
    void testFindGamesByScheduleIdSuccess() throws Exception {
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

        List<Game> games = List.of(game1, game2);

        // Given
        given(this.gameScheduleService.findGamesByScheduleId(scheduleId)).willReturn(games);

        GameDTO gameDTO = new GameDTO(1, 1, "2025-02-02", "Carter", "Baylor", Boolean.TRUE);

        given(this.gameToGameDTOConverter.convert(game1)).willReturn(gameDTO);

        // When and Then
        this.mockMvc.perform(get(this.baseUrl + "/gameSchedule/" + scheduleId + "/game")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Found all games with scheduleId " + scheduleId))
                .andExpect(jsonPath("$.data").isArray());
    }

}
