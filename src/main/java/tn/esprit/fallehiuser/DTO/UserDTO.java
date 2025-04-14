package tn.esprit.fallehiuser.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.fallehiuser.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private String roleName;
    private String status; // 👈 Add this
    private LocalDateTime CreatedDate;
    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roleName = user.getRoleName();
        this.status = user.getStatus() != null ? user.getStatus().name() : "PENDING";
// 👈 Assuming User has a getStatus() method
        this.CreatedDate=user.getCreatedDate();
    }
}
