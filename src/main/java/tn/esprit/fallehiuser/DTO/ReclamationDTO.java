package tn.esprit.fallehiuser.DTO;

import java.time.LocalDateTime;

public class ReclamationDTO {
    private Long id;
    private String username;
    private String subject;
    private String description;
    private String status;
    private LocalDateTime createdDate;

    // Constructor
    public ReclamationDTO(Long id, String username, String subject, String description, String status, LocalDateTime createdDate) {
        this.id = id;
        this.username = username;
        this.subject = subject;
        this.description = description;
        this.status = status;
        this.createdDate = createdDate;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getSubject() {
        return subject;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
