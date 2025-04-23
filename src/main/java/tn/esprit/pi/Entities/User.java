package tn.esprit.pi.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String telephone;
    private String adresse;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    @OneToMany(mappedBy = "client")
    @JsonIgnore
    private List<Commande> commandes;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Livraison> livraisonsClient;

    @OneToMany(mappedBy = "transporteur")  // "transporteur" correspond au nom de la propriété dans Livraison
    @JsonIgnore
    private List<Livraison> livraisonsTransporteur;


    @Column(unique = true, nullable = false)
    private String username;
    private String delegation;
    private int nbLivraisons = 0;

    public String getDelegation() {
        return delegation;
    }

    public void setDelegation(String delegation) {
        this.delegation = delegation;
    }

    public int getNbLivraisons() {
        return nbLivraisons;
    }

    public void setNbLivraisons(int nbLivraisons) {
        this.nbLivraisons = nbLivraisons;
    }

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;


    // New column to store the role name directly
    @Column(name = "role_name", nullable = false)
    private String roleName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ROLE", referencedColumnName = "id")
    @JsonManagedReference
    private Role role;


    private boolean accountLocked;
    private boolean enabled;

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
            System.out.println("Role is present. Role name: " + role.getRoleName());  // Log role name
            this.roleName = role.getRoleName().name();  // Set roleName to the actual Role name
        } else {
            System.out.println("Role is null!");  // Log if role is null
        }

        // Ensure createdDate is set before persisting
        if (createdDate == null) {
            createdDate = LocalDateTime.now(); // Automatically set createdDate
        }
    }


    /* Principal interface implementation */

    public String getName() {
        return username;  // Using username as the principal name for login
    }


    public boolean implies(javax.security.auth.Subject subject) {
        return false;
    }



    public String getPassword() {
        return password;  // Return actual password
    }


    public String getUsername() {
        return username;  // Use username for login
    }


    public boolean isAccountNonExpired() {
        return true;
    }


    public boolean isAccountNonLocked() {
        return !accountLocked;
    }



    public boolean isCredentialsNonExpired() {
        return true;
    }


    public boolean isEnabled() {
        return enabled;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public LocalDateTime getResetTokenExpiry() {
        return resetTokenExpiry;
    }
    public void setResetTokenExpiry(LocalDateTime resetTokenExpiry) {
        this.resetTokenExpiry = resetTokenExpiry;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }
    public String getResetToken() {
        return resetToken;
    }

}
