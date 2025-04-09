package tn.esprit.fallehiuser.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.fallehiuser.DTO.AuthenticationResponse;
import tn.esprit.fallehiuser.DTO.PasswordResetRequest;
import tn.esprit.fallehiuser.DTO.SignInRequest;
import tn.esprit.fallehiuser.DTO.SignUpRequest;
import tn.esprit.fallehiuser.Email.EmailTemplateName;
import tn.esprit.fallehiuser.Services.AuthServiceImpl;
import jakarta.validation.Valid;
import jakarta.mail.MessagingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tn.esprit.fallehiuser.Services.EmailService;
import tn.esprit.fallehiuser.Services.UserServiceImpl;
import tn.esprit.fallehiuser.model.RoleName;
import tn.esprit.fallehiuser.model.User;

import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/auth")
@Tag(name="Authentication")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl service;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final EmailService emailService;
    private final UserServiceImpl userServiceImpl;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(@RequestBody @Valid SignUpRequest request) {
        try {
            service.register(request); // Handles user creation + token + email
            return ResponseEntity.accepted().body("User registered. Please check your email to activate your account.");
        } catch (IllegalStateException e) {
            logger.error("Error during registration: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already registered.");
        } catch (MessagingException e) {
            logger.error("Error sending email: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending activation email.");
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

    private String generateRandomCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";  // Allowed characters
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        // Generate the random code
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }

        return sb.toString();
    }


    @PostMapping("/authenticate")
    @Operation(summary = "User authentication", description = "Authenticates a user and returns a JWT token")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid SignInRequest request) {
        try {
            logger.info("Authenticating user with email or username: {}", request.getEmail() != null ? request.getEmail() : request.getUsername());
            AuthenticationResponse response = service.authenticate(request);
            logger.info("Authentication successful, returning JWT token.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Authentication failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @GetMapping("/activate-account")
    @Operation(summary = "Account activation", description = "Activates the user's account using the provided token")
    public ResponseEntity<?> confirm(@RequestParam String token) {
        try {
            service.activateAccount(token);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> requestBody) {
        try {
            String email = requestBody.get("email");
            service.initiatePasswordReset(email);
            return ResponseEntity.ok("Password reset link sent.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest request) {
        try {
            service.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok("Password reset successful.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }







}