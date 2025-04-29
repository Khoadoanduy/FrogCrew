package edu.tcu.cs.frogcrewbackend.crew;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.frogcrewbackend.crew.dto.CrewRequestDTO;
import edu.tcu.cs.frogcrewbackend.game.Game;
import edu.tcu.cs.frogcrewbackend.member.Member;
import edu.tcu.cs.frogcrewbackend.system.StatusCode;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class CrewListControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    CrewListService crewListService;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @Test
    void testAddCrewListToGame() throws Exception {
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

        CrewRequestDTO crewRequestDTO = new CrewRequestDTO(1, 2, 1, "CAMERA");
        List<CrewRequestDTO> crewRequestDTOList = List.of(crewRequestDTO);

        CrewList crewList = new CrewList();
        crewList.setGame(game1);
        crewList.setCrewedUsers(List.of(cu1));

        String json = objectMapper.writeValueAsString(crewRequestDTOList);

        given(crewListService.addCrewListToGame(1, crewRequestDTOList)).willReturn(crewList);

        // When + Then
        mockMvc.perform(post(baseUrl + "/crewSchedule/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("CrewList added to game 1"))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @Test
    void testFindCrewListByGameId() throws Exception {
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

        // Given
        given(this.crewListService.findCrewListByGameId(1)).willReturn(crewList);

        // When and Then
        this.mockMvc.perform(get(this.baseUrl + "/crewList/" + 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Found crew list with gameId 1"))
                .andExpect(jsonPath("$.data.gameId").value(1));
    }
}
