package tn.esprit.fallehiuser.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.fallehiuser.Repository.UserRepository;
import tn.esprit.fallehiuser.model.RoleName;
import tn.esprit.fallehiuser.model.User;
import tn.esprit.fallehiuser.DTO.UserDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    public List<UserDTO> getUsersByRole(RoleName roleName) {
        List<User> users = userRepository.findByRole_RoleName(roleName);
        return users.stream()
                .map(user -> new UserDTO(user))  // Map each User to a UserDTO
                .collect(Collectors.toList());
    }
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserDTO(user)) // convert entity to DTO
                .collect(Collectors.toList());
    }

}
