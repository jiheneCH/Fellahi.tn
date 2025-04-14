package tn.esprit.fallehiuser.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.fallehiuser.DTO.AuthenticationResponse;
import tn.esprit.fallehiuser.DTO.PasswordResetRequest;
import tn.esprit.fallehiuser.DTO.SignInRequest;
import tn.esprit.fallehiuser.DTO.SignUpRequest;
import tn.esprit.fallehiuser.Services.AuthServiceImpl;
import tn.esprit.fallehiuser.Services.EmailService;
import tn.esprit.fallehiuser.Services.UserServiceImpl;
import tn.esprit.fallehiuser.Services.RecaptchaService;

import jakarta.validation.Valid;
import jakarta.mail.MessagingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Random;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl service;
    private final EmailService emailService;
    private final UserServiceImpl userServiceImpl;
    private final RecaptchaService recaptchaService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody @Valid SignUpRequest request) {
        try {
            // reCAPTCHA verification
            System.out.println("📩 Received reCAPTCHA token: " + request.getRecaptchaToken());
            boolean isHuman = recaptchaService.validateToken(request.getRecaptchaToken());
            if (!isHuman) {
                logger.warn("reCAPTCHA validation failed.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "reCAPTCHA verification failed. Please try again."));
            }

            // Handle user registration
            service.register(request);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "User registered. Please check your email to activate your account."));

        } catch (IllegalStateException e) {
            logger.error("Error during registration: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Email is already registered."));
        } catch (MessagingException e) {
            logger.error("Error sending email: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error sending activation email."));
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred."));
        }
    }

    @PostMapping("/authenticate")
    @Operation(summary = "User authentication", description = "Authenticates a user and returns a JWT token")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid SignInRequest request) {
        try {
            logger.info("Authenticating user: {}", request.getEmail() != null ? request.getEmail() : request.getUsername());
            AuthenticationResponse response = service.authenticate(request);
            logger.info("Authentication successful.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Authentication failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/activate-account")
    public ResponseEntity<?> confirm(@RequestBody Map<String, String> request) {
        try {
            service.activateAccount(request.get("token"));
            return ResponseEntity.ok().body(Map.of("message", "Account activated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
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
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody PasswordResetRequest request) {
        try {
            service.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok(Map.of("message", "Password reset successful."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Optional utility method (not used currently, but kept for future)
    private String generateRandomCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }
        return sb.toString();
    }
}
