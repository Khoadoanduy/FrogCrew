package edu.cs.tcu.frog_crew_application.repository;

import edu.cs.tcu.frog_crew_application.entity.Availability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    // Find all availabilities for a specific crew member
    List<Availability> findByCrewMemberName(String crewMemberName);

    // Find all availabilities for a specific game
    List<Availability> findByGameId(Long gameId);
}
