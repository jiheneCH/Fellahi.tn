package tn.esprit.fallehiuser.Controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.fallehiuser.Repository.UserRepository;
import tn.esprit.fallehiuser.Sercurity.JWTUtil;
import tn.esprit.fallehiuser.Services.UserServiceImpl;
import tn.esprit.fallehiuser.model.RoleName;
import tn.esprit.fallehiuser.DTO.UserDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import tn.esprit.fallehiuser.model.User;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/users")
public class UserController {

    private final JWTUtil jwtUtil;

    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private UserRepository userRepository;

    public UserController(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PreAuthorize("hasRole('ADMIN')") // 🔐 Only allow users with ADMIN role
    @GetMapping("/role/{roleName}")
    @Operation(summary = "Get users by role", description = "Fetches users based on their assigned role")
    public ResponseEntity<?> getUsersByRole(@PathVariable RoleName roleName) {
        List<UserDTO> userDTOs = userServiceImpl.getUsersByRole(roleName);

        if (userDTOs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("No users with this role found.");
        }

        return ResponseEntity.ok(userDTOs);
    }
    @PreAuthorize("hasRole('ADMIN')") // 🔐 Restrict to ADMIN users
    @GetMapping("")
    @Operation(summary = "Get all users", description = "Fetches a list of all registered users")
    public ResponseEntity<?> getAllUsers() {
        List<UserDTO> allUsers = userServiceImpl.getAllUsers();

        if (allUsers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("No users found.");
        }

        return ResponseEntity.ok(allUsers);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search/username/{username}")
    @Operation(summary = "Search users by partial username", description = "Fetches users whose username contains the given string")
    public ResponseEntity<?> searchUsersByUsername(@PathVariable String username) {
        List<UserDTO> users = userServiceImpl.findUsersByPartialUsername(username);

        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("No users found with this username.");
        }

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}/profile-picture")
    public ResponseEntity<byte[]> getUserProfilePicture(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        byte[] image = user.getProfilePicture();

        if (image == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // or PNG
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }



}
