package edu.cs.tcu.frog_crew_application.controller;

import edu.cs.tcu.frog_crew_application.dto.AvailabilityDTO;
import edu.cs.tcu.frog_crew_application.entity.Availability;
import edu.cs.tcu.frog_crew_application.service.AvailabilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availabilities")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @PostMapping
    public ResponseEntity<AvailabilityDTO> submitAvailability(@RequestBody Availability availability) {
        AvailabilityDTO savedAvailability = availabilityService.submitAvailability(availability);
        return ResponseEntity.ok(savedAvailability);
    }

    @GetMapping("/crew-member/{crewMemberName}")
    public ResponseEntity<List<AvailabilityDTO>> getAvailabilityByCrewMember(@PathVariable String crewMemberName) {
        List<AvailabilityDTO> availabilities = availabilityService.getAvailabilityByCrewMember(crewMemberName);
        return ResponseEntity.ok(availabilities);
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<AvailabilityDTO>> getAvailabilityByGameId(@PathVariable Long gameId) {
        List<AvailabilityDTO> availabilities = availabilityService.getAvailabilityByGameId(gameId);
        return ResponseEntity.ok(availabilities);
    }
}
