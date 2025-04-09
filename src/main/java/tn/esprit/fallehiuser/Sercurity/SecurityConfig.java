package tn.esprit.fallehiuser.Sercurity;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final jwtFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Permit authentication-related endpoints (no JWT needed)
                        .requestMatchers("/auth/register", "/auth/authenticate", "/auth/activate-account", "/auth/forgot-password", "/auth/reset-password")
                        .permitAll()

                        // Allow all users to add complaints (no JWT required)
                        .requestMatchers("/reclamations/add")
                        .permitAll()

                        // Only admins can access the role management and complaint management endpoints
                        .requestMatchers("/users/role/**", "/reclamations/pending", "/reclamations/filter", "/reclamations/filterByDate", "/reclamations/treat/**")
                        .hasRole("ADMIN")

                        // All other requests must be authenticated (JWT required)
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session (for JWT)
                // Apply JWT filter to all requests except /reclamations/add
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter for auth

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
