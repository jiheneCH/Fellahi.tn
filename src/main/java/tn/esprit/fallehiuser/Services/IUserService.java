package tn.esprit.fallehiuser.Services;

import tn.esprit.fallehiuser.DTO.UserDTO;
import tn.esprit.fallehiuser.model.RoleName;
import tn.esprit.fallehiuser.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    User save(User user);
    List<UserDTO> findUsersByPartialUsername(String username);
    List<UserDTO> getUsersByRole(RoleName roleName);
    List<UserDTO> getAllUsers();
}
