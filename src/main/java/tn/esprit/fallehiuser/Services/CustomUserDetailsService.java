package tn.esprit.fallehiuser.Services;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tn.esprit.fallehiuser.model.User;
import tn.esprit.fallehiuser.Repository.UserRepository;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user;

        // Check if the input is an email or username and find user accordingly
        if (usernameOrEmail.contains("@")) { // Assuming it's an email
            user = userRepository.findByEmail(usernameOrEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + usernameOrEmail));
        } else { // Otherwise, treat it as a username
            user = userRepository.findByUsername(usernameOrEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + usernameOrEmail));
        }

        // You can add additional checks for account status, if needed
        if (!user.isEnabled()) {
            throw new DisabledException("User account is disabled.");
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername()) // Use username as the unique identifier
                .password(user.getPassword()) // The encoded password
                .roles(user.getRole().toString()) // No need to add "ROLE_" prefix here
                .build();
    }
}
