package tn.esprit.fallehiuser.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Reclamation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private String subject;

    private LocalDateTime createdAt;

    @ManyToOne
    private User user; // User who submitted the complaint

    @Enumerated(EnumType.STRING)
    private ReclamationStatus status = ReclamationStatus.PENDING;

    // NEW FIELDS
    private Long treatedByAdminId = null;               // ID of the admin who treated it
    private String treatedByAdminName = "Not treated";  // Name of the admin who treated it
    private String responseMessage = "Not treated";     // Admin's response message

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

    public Long getTreatedByAdminId() {
        return treatedByAdminId;
    }

    public void setTreatedByAdminId(Long treatedByAdminId) {
        this.treatedByAdminId = treatedByAdminId;
    }

    public String getTreatedByAdminName() {
        return treatedByAdminName;
    }

    public void setTreatedByAdminName(String treatedByAdminName) {
        this.treatedByAdminName = treatedByAdminName;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
