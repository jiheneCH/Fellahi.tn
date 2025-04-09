package tn.esprit.fallehiuser.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignInRequest {

    // Username or Email, one should be provided at a time
    private String username;
    private String email;

    // Password for authentication
    private String password;

}
