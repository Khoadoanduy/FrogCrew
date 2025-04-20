package edu.cs.tcu.frog_crew_application.repository;

import edu.cs.tcu.frog_crew_application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
