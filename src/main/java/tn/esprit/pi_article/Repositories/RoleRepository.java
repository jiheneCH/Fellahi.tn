package tn.esprit.pi_article.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.pi_article.Entities.Role;
import tn.esprit.pi_article.Entities.RoleName;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(RoleName roleName); // ✅ Fix: Should return Optional<Role>

    boolean existsByRoleName(RoleName roleName); // ✅ Add this for checking existence
}
