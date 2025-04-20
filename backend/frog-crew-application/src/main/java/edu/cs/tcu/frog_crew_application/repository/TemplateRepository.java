package edu.cs.tcu.frog_crew_application.repository;

import edu.cs.tcu.frog_crew_application.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {
}