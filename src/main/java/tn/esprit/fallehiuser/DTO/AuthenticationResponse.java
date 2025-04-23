package tn.esprit.fallehiuser.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

import java.util.Map;

@Getter
@Setter
@Builder
public class AuthenticationResponse {
    private String token;
    private Map<String, Object> claims;  // Include claims

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Map<String, Object> getClaims() {
        return claims;
    }

    public void setClaims(Map<String, Object> claims) {
        this.claims = claims;
    }
}