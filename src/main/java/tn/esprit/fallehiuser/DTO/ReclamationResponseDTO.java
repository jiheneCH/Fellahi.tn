package tn.esprit.fallehiuser.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReclamationResponseDTO {
    private Long reclamationId;
    private String adminUsername;
    private String responseMessage;
    private String updatedStatus;
}
