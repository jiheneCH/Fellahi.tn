package tn.esprit.pievent.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tn.esprit.pievent.Entities.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;



import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Builder
@AllArgsConstructor

@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
public class User {

    public User() {
        this.badge = "STANDARD";
        this.reduction = 0.0;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "role_name", nullable = false)
    private String roleName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ROLE", referencedColumnName = "id")
    private Role role;

    private String badge;


    private double reduction;


    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    private String resetToken;

    private LocalDateTime resetTokenExpiry;

    @PrePersist
    @PreUpdate
    protected void onCreate() {
        if (role != null) {
            this.roleName = role.getRoleName().name();
        }
        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }
    }

    // Getters and setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) { this.username = username; }

    public void setPassword(String password) { this.password = password; }



    public LocalDateTime getCreatedDate() { return createdDate; }

    public LocalDateTime getLastModifiedDate() { return lastModifiedDate; }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) { this.lastModifiedDate = lastModifiedDate; }

    public Role getRole() { return role; }

    public void setRole(Role role) { this.role = role; }

    public String getRoleName() { return roleName; }

    public void setRoleName(String roleName) { this.roleName = roleName; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public LocalDateTime getResetTokenExpiry() { return resetTokenExpiry; }

    public void setResetTokenExpiry(LocalDateTime resetTokenExpiry) { this.resetTokenExpiry = resetTokenExpiry; }

    public void setResetToken(String resetToken) { this.resetToken = resetToken; }

    public String getResetToken() { return resetToken; }



//badge et reduction
    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public double getReduction() {
        return reduction;
    }

    public void setReduction(double reduction) {
        this.reduction = reduction;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }



    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Reservation> reservations = new ArrayList<>();


    // Liste des entrées dans la liste d'attente liées au client
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<WaitingListEntry> waitingListEntries;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<WaitingListEntry> getWaitingListEntries() {
        return waitingListEntries;
    }

    public void setWaitingListEntries(List<WaitingListEntry> waitingListEntries) {
        this.waitingListEntries = waitingListEntries;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
