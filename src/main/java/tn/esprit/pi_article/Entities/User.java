package tn.esprit.pi_article.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
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
public class User {



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
                this.roleName = role.getRoleName().name();
            }
            if (createdDate == null) {
                createdDate = LocalDateTime.now();
            }

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


}
