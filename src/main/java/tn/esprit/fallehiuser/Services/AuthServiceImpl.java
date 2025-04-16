package tn.esprit.fallehiuser.Services;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import tn.esprit.fallehiuser.DTO.AuthenticationResponse;
import tn.esprit.fallehiuser.DTO.SignInRequest;
import tn.esprit.fallehiuser.DTO.SignUpRequest;
import tn.esprit.fallehiuser.Email.EmailTemplateName;
import tn.esprit.fallehiuser.Execption.*;
import tn.esprit.fallehiuser.model.*;
import tn.esprit.fallehiuser.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${mailing.frontend.activation-url}")
    private String activationUrl;
    private final jwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    public void register(SignUpRequest request) throws MessagingException {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new EmailAlreadyRegisteredException("Email is already registered");
        }

        RoleName requestedRole;
        try {
            requestedRole = RoleName.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException("Invalid role: " + request.getRole());
        }

        var userRole = roleRepository.findByRoleName(requestedRole)
                .orElseThrow(() -> new IllegalStateException("Role not found: " + requestedRole));

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .role(userRole)
                .build();

        userRepository.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) {
        String token = generateAndSaveActivationToken(user);
        String confirmationUrl = activationUrl + "?token=" + token;
        logger.info("Sending activation email to user: {} with token: {}", user.getEmail(), token);

        try {
            emailService.sendActivationEmail(
                    user.getEmail(),
                    user.getUsername(),
                    confirmationUrl,
                    token,
                    "Activate Your Account"
            );
        } catch (EmailSendingException e) {
            logger.error("Error sending activation email to {}: {}", user.getEmail(), e.getMessage());
            throw new EmailSendingException("Failed to send activation email to " + user.getEmail(), e);
        }
    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationCode(6);
        logger.info("Generated activation token: {}", generatedToken);

        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(10))
                .user(user)
                .build();
        tokenRepository.save(token);

        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }

    public AuthenticationResponse authenticate(SignInRequest request) {
        Optional<User> userOptional = Optional.empty();

        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            logger.info("Attempting authentication with email: {}", request.getEmail());
            userOptional = userRepository.findByEmail(request.getEmail());
        } else if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            logger.info("Attempting authentication with username: {}", request.getUsername());
            userOptional = userRepository.findByUsername(request.getUsername());
        } else {
            logger.error("No email or username provided.");
            throw new IllegalArgumentException("Either email or username must be provided.");
        }

        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found with the provided email or username."));

        if (!user.isEnabled()) {
            logger.warn("Account is not activated for user: {}", user.getUsername());
            throw new AccountNotActivatedException("Account is not activated. Please check your email to activate your account.");
        }

        if (user.isAccountLocked()) {
            logger.warn("Account is locked for user: {}", user.getUsername());
            throw new AccountLockedException("Account is locked. Please contact support.");
        }

        try {
            logger.info("Authenticating user: {}", user.getUsername());
            var auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail() != null ? request.getEmail() : request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            logger.error("Invalid credentials for user: {}", request.getUsername());
            throw new InvalidCredentialsException("Invalid password.");
        }

        // Create claims map
        var claims = new HashMap<String, Object>();
        claims.put("Username", user.getUsername());
        claims.put("role", user.getRoleName());

        // Generate JWT token
        String jwtToken = jwtService.generateToken(claims, user);
        logger.info("Generated JWT token for user: {}", user.getUsername());

        // Extract claims from JWT
        Map<String, Object> extractedClaims = jwtService.extractClaims(jwtToken);

        // Return authentication response with token and claims
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .claims(extractedClaims)  // Add claims to response
                .build();
    }


    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenExpiredException("Invalid token"));

        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new TokenExpiredException("Activation token has expired. A new token has been sent to your email.");
        }

        if(savedToken.getValidatedAt() != null) {
            throw new IllegalStateException("Account already activated");
        }

        User user = savedToken.getUser();
        if(user.isEnabled()) {
            throw new IllegalStateException("Account already activated");
        }

        user.setEnabled(true);
        user.setAccountLocked(false);
        userRepository.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }

    public void initiatePasswordReset(String email) throws MessagingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + email));

        if (user.getResetTokenExpiry() != null &&
                user.getResetTokenExpiry().isAfter(LocalDateTime.now().minusMinutes(10))) {
            throw new PasswordResetLimitExceededException("You can only reset your password once every 30 days.");
        }

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);

        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));

        userRepository.save(user);

        String resetLink = "http://localhost:4200/reset-password?token=" + token;
        emailService.send(
                user.getEmail(),
                emailService.buildEmail("Reset Your Password", resetLink)
        );
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset token"));

        // Add token expiration check
        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Reset token has expired");
        }

        // Only update necessary fields
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        // Add this to prevent role updates
        userRepository.save(user);
    }
}
