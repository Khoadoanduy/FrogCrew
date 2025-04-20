package edu.cs.tcu.frog_crew_application.dto;

public class CrewMemberProfileDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String role;
    private String qualifiedPosition;

    // Constructor
    public CrewMemberProfileDTO(String firstName, String lastName, String email, String phoneNumber, String role, String qualifiedPosition) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.qualifiedPosition = qualifiedPosition;
    }

    // Getters
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getRole() {
        return role;
    }
    public String getQualifiedPosition() {
        return qualifiedPosition;
    }
}
