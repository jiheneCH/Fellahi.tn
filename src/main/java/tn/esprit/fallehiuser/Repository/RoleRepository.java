package tn.esprit.fallehiuser.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.fallehiuser.model.Role;
import tn.esprit.fallehiuser.model.RoleName;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(RoleName roleName); // ✅ Fix: Should return Optional<Role>

    boolean existsByRoleName(RoleName roleName); // ✅ Add this for checking existence
}
