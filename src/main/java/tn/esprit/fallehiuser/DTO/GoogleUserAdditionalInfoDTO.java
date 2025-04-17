package tn.esprit.fallehiuser.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.fallehiuser.model.Role;
import tn.esprit.fallehiuser.model.RoleName;

@Getter
@Setter
public class GoogleUserAdditionalInfoDTO {

    @NotBlank(message = "Address cannot be empty")
    private String address;

    @NotBlank(message = "Phone number cannot be empty")
    private String phoneNumber;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    @NotBlank(message = "Governorate cannot be empty")
    private String governorate;

    @NotNull(message = "Role cannot be empty")
    private RoleName role;

    private MultipartFile profilePicture; // Injected directly into the DTO
}
