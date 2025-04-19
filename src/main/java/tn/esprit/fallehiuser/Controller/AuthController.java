package tn.esprit.fallehiuser.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.fallehiuser.DTO.*;
import java.io.IOException;
import tn.esprit.fallehiuser.Execption.EmailAlreadyRegisteredException;
import tn.esprit.fallehiuser.Execption.InvalidRoleException;
import tn.esprit.fallehiuser.Execption.UserAlreadyExistsException;
import tn.esprit.fallehiuser.Execption.UsernameAlreadyTakenException;
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

import java.util.HashMap;
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
    private final AuthServiceImpl authServiceImpl;

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

            // Attempt user registration
            service.register(request);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "✅ User registered successfully. Please check your email to activate your account."));

        } catch (EmailAlreadyRegisteredException e) {
            logger.warn("Registration failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "This email is already registered."));
        } catch (UsernameAlreadyTakenException e) {
            logger.warn("Registration failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "This username is already taken."));
        } catch (InvalidRoleException e) {
            logger.warn("Registration failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid role selected."));
        } catch (IllegalStateException e) {
            logger.error("Role not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Server configuration error. Please contact support."));
        } catch (MessagingException e) {
            logger.error("Failed to send email: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "We couldn't send the activation email. Please try again later."));
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred. Please try again."));
        }
    }


    @PostMapping("/register-complete")
    public ResponseEntity<Map<String, String>> completeUserProfile(
            @RequestParam String username,
            @ModelAttribute StandardUserAdditionalInfoDTO additionalInfo
    ) {
        Map<String, String> response = new HashMap<>();
        try {
            authServiceImpl.completeUserProfile(
                    username,
                    additionalInfo.getPhoneNumber(),
                    additionalInfo.getAddress(),
                    additionalInfo.getGovernorate(),
                    additionalInfo.getProfileImage()
            );
            response.put("message", "Profile completed successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            e.printStackTrace();
            response.put("message", "Failed to complete profile: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



    @PostMapping(value = "/google-signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> googleSignUp(@RequestBody GoogleSignUpRequest request) {
        try {
            GoogleRegistrationResponse response = service.registerWithGoogle(request.getEmail(), request.getUsername());
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", e.getMessage()));
        }
    }




    @PutMapping(value = "/google-complete", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> completeGoogleRegistration(
            @RequestParam String username,
            @ModelAttribute GoogleUserAdditionalInfoDTO info) {

        try {
            service.completeGoogleUserProfile(username, info);
            return ResponseEntity.ok(Map.of("message", "✅ Google user registration completed successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
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
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> requestBody) {
        try {
            String email = requestBody.get("email");
            service.initiatePasswordReset(email);
            return ResponseEntity.ok(Map.of("message", "Password reset instructions sent to your email."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
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
