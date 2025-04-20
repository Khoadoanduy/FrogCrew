package edu.cs.tcu.frog_crew_application.repository;

import edu.cs.tcu.frog_crew_application.entity.CrewAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CrewAssignmentRepository extends JpaRepository<CrewAssignment, Long> {

    @Query("SELECT ca FROM CrewAssignment ca WHERE ca.game.id = :gameId")
    List<CrewAssignment> findByGameId(@Param("gameId") Long gameId);

    @Query("SELECT ca FROM CrewAssignment ca WHERE ca.game.opponent = :opponent")
    List<CrewAssignment> findByGameOpponent(@Param("opponent") String opponent);

    @Query("SELECT ca FROM CrewAssignment ca WHERE ca.user.firstName = :firstName AND ca.game.gameDate > :today")
    List<CrewAssignment> findFutureAssignmentsByCrewMember(@Param("firstName") String firstName, @Param("today") LocalDate today);
}
