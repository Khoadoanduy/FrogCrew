package edu.cs.tcu.frog_crew_application.controller;

import edu.cs.tcu.frog_crew_application.entity.CrewSchedule;
import edu.cs.tcu.frog_crew_application.service.CrewScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/crewSchedule")
public class CrewScheduleController {

    private final CrewScheduleService crewScheduleService;

    @Autowired
    public CrewScheduleController(CrewScheduleService crewScheduleService) {
        this.crewScheduleService = crewScheduleService;
    }

    @PostMapping("/{gameId}")
    public ResponseEntity<CrewSchedule> createCrewSchedule(@PathVariable Long gameId, @RequestBody CrewSchedule crewSchedule) {
        CrewSchedule createdSchedule = crewScheduleService.createCrewSchedule(gameId, crewSchedule);
        return ResponseEntity.ok(createdSchedule);
    }
}