package edu.tcu.cs.frogcrewbackend.availability;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.frogcrewbackend.system.StatusCode;
import org.aspectj.lang.annotation.Before;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class AvailabilityControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    AvailabilityService availabilityService;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    ArrayList<Availability> availList;

    @BeforeEach
    void setUp() {
        Availability avail1 = new Availability();
        avail1.setUserId(1);
        avail1.setGameId(1);
        avail1.setAvailability(true);
        avail1.setComment("alo");

        Availability avail2 = new Availability();
        avail2.setUserId(1);
        avail2.setGameId(2);
        avail2.setAvailability(false);

        this.availList = new ArrayList<>();
        this.availList.add(avail1);
        this.availList.add(avail2);
    }

    @Test
    void testAddAvailability() throws Exception{
        Availability availability = new Availability();
        availability.setUserId(1);
        availability.setGameId(1);
        availability.setAvailability(true);
        availability.setComment("alo");

        String json = this.objectMapper.writeValueAsString(availability);

        // Given
        given(this.availabilityService.addAvailability(Mockito.any(Availability.class))).willReturn(availability);

        // When and Then
        this.mockMvc.perform(
                post(this.baseUrl + "/availability")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("New availability added"))
                .andExpect(jsonPath("$.data.userId").value(1));
    }
}
