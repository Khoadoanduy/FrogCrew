package edu.cs.tcu.frog_crew_application.service;

import edu.cs.tcu.frog_crew_application.repository.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TemplateService {

    private final TemplateRepository templateRepository;

    @Autowired
    public TemplateService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    public void deleteTemplate(Long templateId) {
        if (!templateRepository.existsById(templateId)) {
            throw new IllegalArgumentException("Template with ID " + templateId + " not found");
        }
        templateRepository.deleteById(templateId);
    }
}