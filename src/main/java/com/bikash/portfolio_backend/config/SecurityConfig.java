package com.bikash.portfolio_backend.config;

import com.bikash.portfolio_backend.security.JwtAuthenticationEntryPoint;
import com.bikash.portfolio_backend.security.JwtAuthenticationFilter;
import com.bikash.portfolio_backend.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    @Lazy
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/auth/register",
                    "/auth/login",
                    "/auth/refresh",
                    "/auth/forgot-password",
                    "/auth/reset-password",
                    "/auth/verify-email",
                    "/auth/google",
                    "/auth/google/user",
                    "/oauth2/authorization/**",
                    "/login/oauth2/code/**",
                    "/test/**",
                    "/swagger-ui/**",
                    "/api-docs/**",
                    "/actuator/health",
                    "/actuator/info",
                    "/health",
                    "/swagger-ui.html",
                    "/webjars/**",
                    "/v3/api-docs/**",
                    "/swagger-ui/**"
                ).permitAll()
                
                .requestMatchers("GET", "/projects", "/projects/**").permitAll()
                .requestMatchers("GET", "/skills", "/skills/**").permitAll()
                .requestMatchers("GET", "/status/current").permitAll()
                

                .requestMatchers("POST", "/contact").permitAll()
                

                .requestMatchers("GET", "/auth/profile").permitAll()
                
                .requestMatchers("/auth/google/allowed-emails").hasRole("ADMIN")
                
                .requestMatchers("/auth/change-password").authenticated()
                
                .requestMatchers("POST", "/projects", "/projects/**").hasRole("ADMIN")
                .requestMatchers("PUT", "/projects", "/projects/**").hasRole("ADMIN")
                .requestMatchers("DELETE", "/projects", "/projects/**").hasRole("ADMIN")
                
                .requestMatchers("POST", "/skills", "/skills/**").hasRole("ADMIN")
                .requestMatchers("PUT", "/skills", "/skills/**").hasRole("ADMIN")
                .requestMatchers("DELETE", "/skills", "/skills/**").hasRole("ADMIN")
                
                .requestMatchers("POST", "/status", "/status/**").hasRole("ADMIN")
                .requestMatchers("PUT", "/status", "/status/**").hasRole("ADMIN")
                .requestMatchers("DELETE", "/status", "/status/**").hasRole("ADMIN")
                
                .requestMatchers("GET", "/contact/**").hasRole("ADMIN")
                .requestMatchers("PUT", "/contact/**").hasRole("ADMIN")
                .requestMatchers("DELETE", "/contact/**").hasRole("ADMIN")
                
                .anyRequest().authenticated()
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(authorization -> authorization
                    .baseUri("/oauth2/authorization")
                )
                .redirectionEndpoint(redirection -> redirection
                    .baseUri("/login/oauth2/code/*")
                )
                .successHandler(oAuth2SuccessHandler)
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
} 