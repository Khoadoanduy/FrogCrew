package edu.tcu.cs.frogcrewbackend.crew;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.frogcrewbackend.crew.converter.CrewedUserToCrewedUserDTOConverter;
import edu.tcu.cs.frogcrewbackend.crew.dto.CrewedUserDTO;
import edu.tcu.cs.frogcrewbackend.game.Game;
import edu.tcu.cs.frogcrewbackend.member.Member;
import edu.tcu.cs.frogcrewbackend.system.StatusCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class CrewedUserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    CrewedUserService crewedUserService;

    @MockitoBean
    CrewedUserToCrewedUserDTOConverter crewedUserToCrewedUserDTOConverter;

    List<CrewedUserDTO> crewedUsers;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() {
        CrewedUserDTO cu1 = new CrewedUserDTO(1, 1, 1, "PRODUCER", "Kate Bednarz", "12:00", "CONTROL ROOM");
        CrewedUserDTO cu2 = new CrewedUserDTO(2, 1, 1, "PRODUCER", "Dave Park", "12:00", "CONTROL ROOM");

        this.crewedUsers = new ArrayList<>();
        crewedUsers.add(cu1);
        crewedUsers.add(cu2);
    }

    @AfterEach
    void tearDown(){}

    @Test
    void testFindCrewedUsersByGameAndPositionSuccess() throws Exception {
        Member mem1 = new Member();
        mem1.setId(1);
        mem1.setFirstName("Bruce");
        mem1.setLastName("Wayne");
        mem1.setEmail("bw@gmail.com");
        mem1.setPhoneNumber("1234567890");
        mem1.setPassword("password1");
        mem1.setRole("MEMBER");
        mem1.setPositions("DIRECTOR");

        Game game1 = new Game();
        game1.setGameId(1);
        game1.setGameDate("2024-10-10");
        game1.setVenue("Amon G. Carter");
        game1.setOpponent("Texas Longhorn");
        game1.setFinalized(Boolean.FALSE);

        CrewedUser cu1 = new CrewedUser();
        cu1.setCrewedUserId(0);
        cu1.setUser(mem1);
        cu1.setGame(game1);
        cu1.setPosition("DIRECTOR");
        cu1.setReportTime("12:00");
        cu1.setReportLocation("CONTROL ROOM");

        CrewedUserDTO cuDTO = new CrewedUserDTO(1, 1, 1, "Bruce Wayne", "DIRECTOR", "", "");

        // Given
        given(this.crewedUserService.findCrewedUsersByGameAndPosition(1, "DIRECTOR")).willReturn(List.of(cu1));
        given(this.crewedUserToCrewedUserDTOConverter.convert(cu1)).willReturn(cuDTO);

        // When and Then
        this.mockMvc.perform(get(this.baseUrl + "/crewedUser/1/DIRECTOR")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.data[0].position").value("DIRECTOR"));
    }
}
