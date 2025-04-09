package tn.esprit.fallehiuser.model;

import jakarta.persistence.*;
import tn.esprit.fallehiuser.model.ReclamationStatus;
import tn.esprit.fallehiuser.model.User;

import java.time.LocalDateTime;

@Entity
public class Reclamation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;  // The complaint's description

    private String subject;  // The subject of the complaint (for filtering)

    private LocalDateTime createdAt;  // The time when the complaint was added

    @ManyToOne
    private User user;  // The user who submitted the complaint

    @Enumerated(EnumType.STRING)
    private ReclamationStatus status = ReclamationStatus.PENDING;  // The status of the complaint

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ReclamationStatus getStatus() {
        return status;
    }

    public void setStatus(ReclamationStatus status) {
        this.status = status;
    }
}
