package tn.esprit.fallehiuser.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.fallehiuser.model.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private String roleName;  // RoleName instead of the full Role object

    // Constructor to convert User to UserDTO
    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roleName = user.getRoleName() != null ? user.getRoleName() : null; // Set RoleName as a string
    }
}
