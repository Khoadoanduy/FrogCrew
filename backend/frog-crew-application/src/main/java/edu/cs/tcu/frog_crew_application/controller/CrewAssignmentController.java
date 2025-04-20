// File: src/main/java/edu/cs/tcu/frog_crew_application/controller/CrewAssignmentController.java
package edu.cs.tcu.frog_crew_application.controller;

import edu.cs.tcu.frog_crew_application.dto.CrewAssignmentCreationDto;
import edu.cs.tcu.frog_crew_application.entity.CrewAssignment;
import edu.cs.tcu.frog_crew_application.service.CrewAssignmentService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/crew-assignments")
public class CrewAssignmentController {

    private final CrewAssignmentService crewAssignmentService;

    @Autowired
    public CrewAssignmentController(CrewAssignmentService crewAssignmentService) {
        this.crewAssignmentService = crewAssignmentService;
    }

    @PostMapping
    public CrewAssignment createCrewAssignment(@RequestBody CrewAssignmentCreationDto creationDto) {
        return crewAssignmentService.saveAssignment(creationDto);
    }
    
    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<CrewAssignment>> getAssignmentsByGame(@PathVariable Long gameId) {
    List<CrewAssignment> crewAssignments = crewAssignmentService.getCrewAssignmentsByGameId(gameId);
    return ResponseEntity.ok(crewAssignments);
}

}
