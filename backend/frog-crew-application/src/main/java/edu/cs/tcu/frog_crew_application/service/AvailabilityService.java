package edu.cs.tcu.frog_crew_application.service;

import edu.cs.tcu.frog_crew_application.dto.AvailabilityDTO;
import edu.cs.tcu.frog_crew_application.entity.Availability;
import edu.cs.tcu.frog_crew_application.repository.AvailabilityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;

    public AvailabilityService(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    public AvailabilityDTO submitAvailability(Availability availability) {
        Availability savedAvailability = availabilityRepository.save(availability);

        // (Simple Notification Step)
        System.out.println("🚨 Notification to Admin: New availability submitted by " + savedAvailability.getCrewMemberName());

        return new AvailabilityDTO(
                savedAvailability.getCrewMemberName(),
                savedAvailability.getGameId(),
                savedAvailability.getAvailable(),
                savedAvailability.getComment()
        );
    }

    public List<AvailabilityDTO> getAvailabilityByCrewMember(String crewMemberName) {
        List<Availability> availabilities = availabilityRepository.findByCrewMemberName(crewMemberName);

        return availabilities.stream()
                .map(a -> new AvailabilityDTO(
                        a.getCrewMemberName(),
                        a.getGameId(),
                        a.getAvailable(),
                        a.getComment()
                ))
                .collect(Collectors.toList());
    }

    public List<AvailabilityDTO> getAvailabilityByGameId(Long gameId) {
        List<Availability> availabilities = availabilityRepository.findByGameId(gameId);

        return availabilities.stream()
                .map(a -> new AvailabilityDTO(
                        a.getCrewMemberName(),
                        a.getGameId(),
                        a.getAvailable(),
                        a.getComment()
                ))
                .collect(Collectors.toList());
    }
}
