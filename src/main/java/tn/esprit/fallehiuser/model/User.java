package tn.esprit.fallehiuser.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tn.esprit.fallehiuser.model.Commande;
import tn.esprit.fallehiuser.model.Livraison;

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
public class User implements UserDetails, Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
private String Name;
private int nbLivraison;
    @Column(unique = true, nullable = false)
    private String username;
    @OneToMany(mappedBy = "client")
    @JsonIgnore
    private List<Commande> commandes;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Livraison> livraisonsClient;

    @OneToMany(mappedBy = "transporteur")  // "transporteur" correspond au nom de la propriété dans Livraison
    @JsonIgnore
    private List<Livraison> livraisonsTransporteur;

    public void setName(String name) {
        Name = name;
    }

    public int getNbLivraison() {
        return nbLivraison;
    }

    public void setNbLivraison(int nbLivraisons) {
        this.nbLivraison = nbLivraisons;
    }

    public List<Commande> getCommandes() {
        return commandes;
    }

    public void setCommandes(List<Commande> commandes) {
        this.commandes = commandes;
    }

    public List<Livraison> getLivraisonsClient() {
        return livraisonsClient;
    }

    public void setLivraisonsClient(List<Livraison> livraisonsClient) {
        this.livraisonsClient = livraisonsClient;
    }

    public List<Livraison> getLivraisonsTransporteur() {
        return livraisonsTransporteur;
    }

    public void setLivraisonsTransporteur(List<Livraison> livraisonsTransporteur) {
        this.livraisonsTransporteur = livraisonsTransporteur;
    }

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "role_name", nullable = false)
    private String roleName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ROLE", referencedColumnName = "id")
    private Role role;

    @Column(nullable = false)
    private boolean isGoogle = false; // Default is standard registration


    private boolean accountLocked;
    private boolean enabled;

    @Enumerated(EnumType.STRING)
    private status status;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    private String resetToken;

    private LocalDateTime resetTokenExpiry;

    // ✅ New fields for the second registration step
    private String address;

    private String phoneNumber;

    private String governorate;

    @Lob // Large Object annotation for binary data
    private byte[] profilePicture;

    @PrePersist
    @PreUpdate
    protected void onCreate() {
        if (role != null) {
            this.roleName = role.getRoleName().name();
        }
        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }
        if (status == null) {
            status = tn.esprit.fallehiuser.model.status.ACTIVE;
        }
    }

    // Principal interface
    @Override
    public String getName() {
        return username;
    }

    @Override
    public boolean implies(javax.security.auth.Subject subject) {
        return false;
    }

    // UserDetails interface
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.getRoleName().name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    // Getters and setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public void setUsername(String username) { this.username = username; }

    public void setPassword(String password) { this.password = password; }

    public boolean isAccountLocked() { return accountLocked; }

    public void setAccountLocked(boolean accountLocked) { this.accountLocked = accountLocked; }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }

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

    public status getStatus() { return status; }

    public void setStatus(status status) { this.status = status; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getGovernorate() { return governorate; }

    public void setGovernorate(String governorate) { this.governorate = governorate; }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }
    public boolean isGoogle() {
        return isGoogle;
    }

    public void setGoogle(boolean google) {
        isGoogle = google;
    }

}
