package tn.esprit.fallehiuser.Repository;




import tn.esprit.fallehiuser.model.RoleName;
import tn.esprit.fallehiuser.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    List<User> findByRole_RoleName(RoleName roleName);
    Optional<User> findByResetToken(String resetToken);

}

