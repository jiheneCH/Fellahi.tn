package tn.esprit.fallehiuser.Sercurity;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tn.esprit.fallehiuser.Services.CustomUserDetailsService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class jwtFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtils;
    private final CustomUserDetailsService userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(jwtFilter.class);
    private final tn.esprit.fallehiuser.Services.jwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        logger.debug("Request URI: {}", requestURI);

        // Skip JWT validation for public endpoints
        if (requestURI.startsWith("/auth/") || requestURI.equals("/Fallehi/reclamations/add")) { // 🔼 Add this condition
            logger.debug("Skipping JWT validation for public endpoint: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        // Extract Bearer token from Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("No Bearer token found in Authorization header for request URI: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7); // Remove "Bearer "
        logger.debug("Extracted token: {}", token);

        try {
            // Get all claims from the token
            Claims claims = jwtUtils.getAllClaimsFromToken(token);
            String username = claims.getSubject();
            String role = (String) claims.get("role");

            // Authenticate only if user is not already authenticated
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.validateToken(token)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, AuthorityUtils.createAuthorityList("ROLE_" + role));
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    logger.debug("Authentication successful for user: {}", username);
                } else {
                    logger.warn("Invalid token for user: {}", username);
                }
            } else {
                logger.debug("User is already authenticated or username is null.");
            }

        } catch (Exception e) {
            logger.error("Error occurred while processing JWT token: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
