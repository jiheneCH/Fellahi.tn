package tn.esprit.fallehiuser.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleRegistrationResponse {
    private String message;
    private String email;
    private String username;

    // ✅ No-args constructor (required by Jackson)
    public GoogleRegistrationResponse() {}

    public GoogleRegistrationResponse(String message, String email, String username) {
        this.message = message;
        this.email = email;
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
