package tn.esprit.fallehiuser.Controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.fallehiuser.Sercurity.JWTUtil;
import tn.esprit.fallehiuser.Services.UserServiceImpl;
import tn.esprit.fallehiuser.model.RoleName;
import tn.esprit.fallehiuser.DTO.UserDTO;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
public class UserController {

    private final JWTUtil jwtUtil;

    @Autowired
    private UserServiceImpl userServiceImpl;

    public UserController(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PreAuthorize("hasRole('ADMIN')") // 🔐 Only allow users with ADMIN role
    @GetMapping("/users/role/{roleName}")
    @Operation(summary = "Get users by role", description = "Fetches users based on their assigned role")
    public ResponseEntity<?> getUsersByRole(@PathVariable RoleName roleName) {
        List<UserDTO> userDTOs = userServiceImpl.getUsersByRole(roleName);

        if (userDTOs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("No users with this role found.");
        }

        return ResponseEntity.ok(userDTOs);
    }
}
