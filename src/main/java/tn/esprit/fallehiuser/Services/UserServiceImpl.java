package tn.esprit.fallehiuser.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.fallehiuser.DTO.UserDTO;
import tn.esprit.fallehiuser.Repository.UserRepository;
import tn.esprit.fallehiuser.model.RoleName;
import tn.esprit.fallehiuser.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

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

    @Override
    public List<UserDTO> getUsersByRole(RoleName roleName) {
        return userRepository.findByRole_RoleName(roleName)
                .stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> findUsersByPartialUsername(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username)
                .stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }


}
