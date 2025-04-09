package tn.esprit.fallehiuser.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

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

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;


    // New column to store the role name directly
    @Column(name = "role_name", nullable = false)
    private String roleName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ROLE", referencedColumnName = "id")
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
    @Override
    public String getName() {
        return username;  // Using username as the principal name for login
    }

    @Override
    public boolean implies(javax.security.auth.Subject subject) {
        return false;
    }

    /* UserDetails interface implementation */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.getRoleName().name()));
    }

    @Override
    public String getPassword() {
        return password;  // Return actual password
    }

    @Override
    public String getUsername() {
        return username;  // Use username for login
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
