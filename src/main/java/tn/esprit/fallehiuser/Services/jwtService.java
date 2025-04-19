package tn.esprit.fallehiuser.Services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class jwtService {

    @Value("${security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    // Generate token from user details
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // Generate token with custom claims
    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return buildToken(claims, userDetails, jwtExpiration);
    }

    // Build JWT token with specified expiration time
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long jwtExpiration) {
        List<String> authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        extraClaims.put("authorities", authorities);  // Store authorities as a claim

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey())
                .compact();
    }

    // Validate if token is valid
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Check if token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract username from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract any claim from token
    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    // Extract all claims from token
    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JWT token", e);  // Improved error handling
        }
    }

    // Validate token
    public boolean validateToken(String token) {
        try {
            Jws<Claims> parsedToken = Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token);

            String alg = parsedToken.getHeader().getAlgorithm();
            System.out.println("🔐 Parsed token with algorithm: " + alg);

            // Ensure the token is not expired and is valid
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("❌ Invalid JWT: " + e.getMessage());
            return false;
        }
    }

    // Extract authorities from token
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return (List<String>) claims.get("authorities");  // Returns the list of roles
    }

    // Extract roles and return them as GrantedAuthority objects for Spring Security
    public List<GrantedAuthority> extractGrantedAuthorities(String token) {
        List<String> roles = extractRoles(token);
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    // Get the signing key (secret key used to sign the JWT)
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract claims as a Map from token
    public Map<String, Object> extractClaims(String token) {
        Claims claims = extractAllClaims(token);
        return claims;
    }
}
