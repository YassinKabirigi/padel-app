package be.ephec.padel_backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Autowired
    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs").permitAll()
                        .requestMatchers("/api/membres/me", "/api/membres/me/**").authenticated()
                        .requestMatchers("/api/participations/me").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/sites/**", "/api/terrains/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/sites/**", "/api/terrains/**").hasAnyAuthority("ROLE_ADMIN_GLOBAL", "ROLE_ADMIN_SITE")
                        .requestMatchers(HttpMethod.PUT, "/api/sites/**", "/api/terrains/**").hasAnyAuthority("ROLE_ADMIN_GLOBAL", "ROLE_ADMIN_SITE")
                        .requestMatchers(HttpMethod.DELETE, "/api/sites/**", "/api/terrains/**").hasAnyAuthority("ROLE_ADMIN_GLOBAL", "ROLE_ADMIN_SITE")
                        .requestMatchers(HttpMethod.GET, "/api/jours-fermeture/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/jours-fermeture/**").hasAnyAuthority("ROLE_ADMIN_GLOBAL", "ROLE_ADMIN_SITE")
                        .requestMatchers(HttpMethod.PUT, "/api/jours-fermeture/**").hasAnyAuthority("ROLE_ADMIN_GLOBAL", "ROLE_ADMIN_SITE")
                        .requestMatchers(HttpMethod.DELETE, "/api/jours-fermeture/**").hasAnyAuthority("ROLE_ADMIN_GLOBAL", "ROLE_ADMIN_SITE")
                        .requestMatchers("/api/penalites/**").hasAnyAuthority("ROLE_ADMIN_GLOBAL", "ROLE_ADMIN_SITE")
                        .requestMatchers(HttpMethod.GET, "/api/membres/*").authenticated()
                        .requestMatchers("/api/membres/**").hasAnyAuthority("ROLE_ADMIN_GLOBAL", "ROLE_ADMIN_SITE")
                        .requestMatchers("/api/administrateurs/**").hasAuthority("ROLE_ADMIN_GLOBAL")
                        .requestMatchers("/api/participations/**").hasAnyAuthority("ROLE_ADMIN_GLOBAL", "ROLE_ADMIN_SITE")
                        .requestMatchers("/api/matches/**").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            throw new UsernameNotFoundException(
                    "Authentification par JWT uniquement - UserDetailsService non utilise");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
