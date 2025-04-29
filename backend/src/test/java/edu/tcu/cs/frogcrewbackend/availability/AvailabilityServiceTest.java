package edu.tcu.cs.frogcrewbackend.availability;

import jakarta.inject.Inject;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureMockMvc
public class AvailabilityServiceTest {

    @Mock
    AvailabilityRepository availabilityRepository;

    @InjectMocks
    AvailabilityService availabilityService;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void testAddAvailability() {
        // Given
        Availability availability = new Availability();
        availability.setUserId(1);
        availability.setGameId(1);
        availability.setAvailability(true);
        availability.setComment("comment");

        given(this.availabilityRepository.save(availability)).willReturn(availability);

        // When
        Availability newAvailability = this.availabilityService.addAvailability(availability);

        // Then
        assertThat(newAvailability.getUserId()).isEqualTo(1);
        assertThat(newAvailability.getGameId()).isEqualTo(1);
        assertThat(newAvailability.getAvailability()).isEqualTo(true);
        assertThat(newAvailability.getComment()).isEqualTo("comment");
        verify(this.availabilityRepository, times(1)).save(availability);
    }
}
