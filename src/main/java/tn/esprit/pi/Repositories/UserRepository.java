package tn.esprit.pi.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.pi.Entities.Role;
import tn.esprit.pi.Entities.RoleName;
import tn.esprit.pi.Entities.User;



import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.roleName = 'TRANSPORTER' AND u.delegation = :delegation")
    List<User> findUserByDelegation(@Param("delegation") String delegation);
    User findByUsername(String username);
    User findById(long id);



}

