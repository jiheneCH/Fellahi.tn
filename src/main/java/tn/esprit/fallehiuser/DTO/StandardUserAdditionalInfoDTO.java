package tn.esprit.fallehiuser.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StandardUserAdditionalInfoDTO {

    @NotBlank(message = "Address cannot be empty")
    private String address;

    @NotBlank(message = "Phone number cannot be empty")
    private String phoneNumber;

    @NotBlank(message = "Governorate cannot be empty")
    private String governorate;
    @NotBlank(message = "Profile Image  cannot be empty")
    private String profileImageUrl;
}
