package edu.cs.tcu.frog_crew_application.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "crew_member_profiles")
public class CrewMemberProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(unique = true)
    private String email;

    private String phoneNumber;

    private String skills; // For now, just a comma-separated string

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Constructors
    public CrewMemberProfile() {}

    public CrewMemberProfile(String fullName, String email, String phoneNumber, String skills, User user) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.skills = skills;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() { return id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
