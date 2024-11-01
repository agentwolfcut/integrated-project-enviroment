package dev.backendintegratedproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import dev.backendintegratedproject.filters.JwtAuthenticFilter;
import dev.backendintegratedproject.services.UserService;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private JwtAuthenticFilter jwtAuthenticFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .authorizeRequests(authorize -> authorize
                        .requestMatchers("/v3/login", "/error", "/v3/token").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v3/boards/{boardID}", "/v3/boards/{boardID}/tasks",
                                "/v3/boards/{boardID}/statuses", "/v3/boards/{boardID}/tasks/{id}",
                                "/v3/boards/{boardID}/statuses/{id}").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/v3/boards/**").permitAll()
                        .anyRequest().authenticated())
                .httpBasic();

        httpSecurity.addFilterBefore(jwtAuthenticFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:5173", "http://ip23kk3.sit.kmutt.ac.th", "http://intproj23.sit.kmutt.ac.th"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            String message = authException.getMessage();
            response.getWriter().write("{\"message\":\"" + message + "\"}");
        };
    }
}