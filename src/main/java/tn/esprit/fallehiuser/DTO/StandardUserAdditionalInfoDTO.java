package tn.esprit.fallehiuser.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class StandardUserAdditionalInfoDTO {

    @NotBlank(message = "Address cannot be empty")
    private String address;

    @NotBlank(message = "Phone number cannot be empty")
    private String phoneNumber;

    @NotBlank(message = "Governorate cannot be empty")
    private String governorate;

    // This will be handled in multipart/form-data
    private MultipartFile profileImage;
}
