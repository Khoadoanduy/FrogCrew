package edu.cs.tcu.frog_crew_application.controller;

import edu.cs.tcu.frog_crew_application.entity.CrewMemberProfile;
import edu.cs.tcu.frog_crew_application.service.CrewMemberProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/crew-members")
public class CrewMemberProfileController {

    private final CrewMemberProfileService crewMemberProfileService;

    public CrewMemberProfileController(CrewMemberProfileService crewMemberProfileService) {
        this.crewMemberProfileService = crewMemberProfileService;
    }

    @PostMapping
    public ResponseEntity<CrewMemberProfile> createProfile(@RequestBody CrewMemberProfile profile) {
        return ResponseEntity.ok(crewMemberProfileService.createProfile(profile));
    }
}
