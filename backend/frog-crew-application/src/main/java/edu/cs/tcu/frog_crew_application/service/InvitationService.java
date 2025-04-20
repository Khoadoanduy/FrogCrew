package edu.cs.tcu.frog_crew_application.service;

import edu.cs.tcu.frog_crew_application.dto.InvitationDTO;
import edu.cs.tcu.frog_crew_application.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class InvitationService {

    private final UserRepository userRepository;

    public InvitationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void inviteCrewMember(InvitationDTO invitationDTO) {
        // Check if email already exists
        boolean emailExists = userRepository.findByEmail(invitationDTO.getEmail()).isPresent();
        if (emailExists) {
            throw new IllegalArgumentException("Email is already registered. Please invite a different email.");
        }

        // Simulate sending email
        System.out.println("📧 Sending invitation to: " + invitationDTO.getEmail());
        System.out.println("Hello " + invitationDTO.getName() + ",");
        System.out.println("You have been invited to join FrogCrew! Please click the link below to register:");
        System.out.println("👉 http://localhost:8080/register?email=" + invitationDTO.getEmail());

        // (Optional: Future real email integration here)
    }
}
