package edu.tcu.cs.frogcrewbackend.game;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GameScheduleRepository extends JpaRepository<GameSchedule, Integer> {
}
