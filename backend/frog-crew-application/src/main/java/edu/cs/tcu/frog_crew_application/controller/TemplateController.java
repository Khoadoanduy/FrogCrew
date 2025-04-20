package edu.cs.tcu.frog_crew_application.controller;

import edu.cs.tcu.frog_crew_application.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/template")
public class TemplateController {

    private final TemplateService templateService;

    @Autowired
    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @DeleteMapping("/{templateId}")
    public ResponseEntity<?> deleteTemplate(@PathVariable Long templateId) {
        templateService.deleteTemplate(templateId);
        return ResponseEntity.ok().body("Template deleted successfully");
    }
}