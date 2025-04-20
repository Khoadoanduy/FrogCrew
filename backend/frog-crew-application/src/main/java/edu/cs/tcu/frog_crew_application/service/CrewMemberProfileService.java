package edu.cs.tcu.frog_crew_application.service;

import edu.cs.tcu.frog_crew_application.entity.CrewMemberProfile;
import edu.cs.tcu.frog_crew_application.entity.User;
import edu.cs.tcu.frog_crew_application.repository.CrewMemberProfileRepository;
import edu.cs.tcu.frog_crew_application.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CrewMemberProfileService {

    private final CrewMemberProfileRepository crewMemberProfileRepository;
    private final UserRepository userRepository;

    public CrewMemberProfileService(CrewMemberProfileRepository crewMemberProfileRepository, UserRepository userRepository) {
        this.crewMemberProfileRepository = crewMemberProfileRepository;
        this.userRepository = userRepository;
    }

    public CrewMemberProfile createProfile(CrewMemberProfile profile) {
        if (profile.getUser() == null || profile.getUser().getId() == null) {
            throw new IllegalArgumentException("User ID must be provided.");
        }
    
        Long userId = profile.getUser().getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
    
        profile.setUser(user);
        return crewMemberProfileRepository.save(profile);
    }
}
