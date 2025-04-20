package edu.cs.tcu.frog_crew_application.controller;

import edu.cs.tcu.frog_crew_application.dto.InvitationDTO;
import edu.cs.tcu.frog_crew_application.service.InvitationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invitations")
public class InvitationController {

    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @PostMapping
    public ResponseEntity<?> inviteCrewMember(@Valid @RequestBody InvitationDTO invitationDTO) {
        invitationService.inviteCrewMember(invitationDTO);
        return ResponseEntity.ok().body("Invitation sent successfully to " + invitationDTO.getEmail());
    }
}
