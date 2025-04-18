package tn.esprit.pi.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.pi.Entities.Role;
import tn.esprit.pi.Entities.RoleName;


import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(RoleName roleName); // ✅ Fix: Should return Optional<Role>

    boolean existsByRoleName(RoleName roleName); // ✅ Add this for checking existence
}
