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

        String requestURI = request.getRequestURI().replace("/Fallehi", "");
        logger.debug("Processing request to: {}", requestURI);

        if (shouldSkipValidation(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Missing Bearer token for: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        processJwtToken(authHeader.substring(7), request);
        filterChain.doFilter(request, response);
    }

    private boolean shouldSkipValidation(String requestURI) {
        return requestURI.startsWith("/auth/") ||
                requestURI.equals("/reclamations/add");
    }

    private void processJwtToken(String token, HttpServletRequest request) {
        try {
            Claims claims = jwtUtils.getAllClaimsFromToken(token);
            String username = claims.getSubject();
            String role = (String) claims.get("role");

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.validateToken(token)) {
                    setAuthentication(userDetails, role, request);
                }
            }
        } catch (Exception e) {
            logger.error("JWT processing error: {}", e.getMessage());
        }
    }

    private void setAuthentication(UserDetails userDetails, String role, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                AuthorityUtils.createAuthorityList("ROLE_" + role)
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}