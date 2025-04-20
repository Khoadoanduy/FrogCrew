package edu.cs.tcu.frog_crew_application.repository;

import edu.cs.tcu.frog_crew_application.entity.CrewSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewScheduleRepository extends JpaRepository<CrewSchedule, Long> {
}