package tn.esprit.pievent.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.pievent.Entities.RoleName;
import tn.esprit.pievent.Entities.User;

import java.util.List;
import java.util.Optional;




    public interface UserRepository extends JpaRepository<User, Long> {
        Optional<User> findByEmail(String email);

        Optional<User> findByUsername(String username);

        boolean existsByEmail(String email);

        boolean existsByUsername(String username);

        List<User> findByRole_RoleName(RoleName roleName);

        Optional<User> findByResetToken(String resetToken);

        List<User> findByUsernameContainingIgnoreCase(String username);

        Optional<User> findById(Long id);


    }
