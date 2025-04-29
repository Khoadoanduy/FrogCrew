package edu.tcu.cs.frogcrewbackend.member;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByEmail(String email);
}
