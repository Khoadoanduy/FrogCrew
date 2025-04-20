package edu.cs.tcu.frog_crew_application.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First Name is required")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Phone Number is required")
    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}", message = "Phone number must match the format 999-999-9999")
    private String phoneNumber;

    @NotBlank(message = "Role is required")
    private String role;

    @NotBlank(message = "Qualified Position is required")
    private String qualifiedPosition;

    // Constructors
    public User() {}

    public User(String firstName, String lastName, String email, String password, String phoneNumber, String role, String qualifiedPosition) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.qualifiedPosition = qualifiedPosition;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    // First Name
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Last Name
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Email
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    // Password
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    // Phone Number
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Role
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    // Qualified Position
    public String getQualifiedPosition() {
        return qualifiedPosition;
    }
    public void setQualifiedPosition(String qualifiedPosition) {
        this.qualifiedPosition = qualifiedPosition;
    }
}
