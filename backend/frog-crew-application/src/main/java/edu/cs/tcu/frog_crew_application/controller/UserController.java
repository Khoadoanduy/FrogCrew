package edu.cs.tcu.frog_crew_application.controller;

import edu.cs.tcu.frog_crew_application.dto.CrewMemberProfileDTO;
import edu.cs.tcu.frog_crew_application.dto.UserRegistrationDTO;
import edu.cs.tcu.frog_crew_application.dto.UserUpdateDTO;
import edu.cs.tcu.frog_crew_application.entity.User;
import edu.cs.tcu.frog_crew_application.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // Constructor
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        User createdUser = userService.registerUser(userRegistrationDTO);
        return ResponseEntity.ok(createdUser);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        User updatedUser = userService.updateUser(id, userUpdateDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // ⭐️ New endpoint for Use Case 3
    @GetMapping("/{id}")
    public ResponseEntity<CrewMemberProfileDTO> getCrewMemberProfile(@PathVariable Long id) {
        CrewMemberProfileDTO profile = userService.getCrewMemberProfile(id);
        return ResponseEntity.ok(profile);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().body("Crew Member deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage()); // 409 Conflict
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCrewMembers() {
        try {
            List<User> crewMembers = userService.getAllCrewMembers();
            return ResponseEntity.ok(crewMembers);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
