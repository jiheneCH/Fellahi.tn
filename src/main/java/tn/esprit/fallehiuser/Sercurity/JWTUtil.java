package tn.esprit.fallehiuser.Sercurity;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {

    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    private static final long EXPIRATION_TIME = 86400000; // 24 hours

    @PostConstruct
    public void logSecretKey() {
        System.out.println("✅ JWT Secret Key loaded: " + SECRET_KEY);  // Debug only
    }


    // Generate signing key from Base64-decoded secret key
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); // 🔑 Base64 decode
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract claims from the token
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extract the username from the token
    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    // Extract the role from the token
    public String getRoleFromToken(String token) {
        return getAllClaimsFromToken(token).get("role", String.class);
    }
}