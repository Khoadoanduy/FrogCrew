package edu.cs.tcu.frog_crew_application.repository;

import edu.cs.tcu.frog_crew_application.entity.CrewMemberProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewMemberProfileRepository extends JpaRepository<CrewMemberProfile, Long> {
}
