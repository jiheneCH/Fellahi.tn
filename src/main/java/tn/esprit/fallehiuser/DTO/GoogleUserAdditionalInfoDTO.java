package tn.esprit.fallehiuser.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import tn.esprit.fallehiuser.model.Role;

@Getter
@Setter
public class GoogleUserAdditionalInfoDTO {

    @NotBlank(message = "Address cannot be empty")
    private String address;

    @NotBlank(message = "Phone number cannot be empty")
    private String phoneNumber;

    @NotBlank(message = "Governorate cannot be empty")
    private String governorate;

    private Role role; // enum
}
