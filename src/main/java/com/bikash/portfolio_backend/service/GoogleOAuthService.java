package com.bikash.portfolio_backend.service;

import com.bikash.portfolio_backend.dto.auth.AuthResponse;
import com.bikash.portfolio_backend.entity.User;
import com.bikash.portfolio_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    
    public AuthResponse processGoogleAuth(String email, String name, String googleId) {
        log.info("Processing Google OAuth for email: {}", email);
        
        Optional<User> existingUser = userRepository.findByEmail(email);
        
        User user;
        if (existingUser.isPresent()) {
            user = handleExistingUser(existingUser.get(), name, googleId);
        } else {
            user = createGoogleUser(email, name, googleId);
            log.info("Created new Google user: {}", email);
        }
        
        return generateAuthResponse(user);
    }

    
    private User handleExistingUser(User existingUser, String googleName, String googleId) {
        log.info("Existing user found: {}", existingUser.getEmail());
        
        boolean isGoogleUser = "google".equals(existingUser.getProvider()) || 
                              (existingUser.getGoogleId() != null);
        
        if (isGoogleUser) {
            if (!googleName.equals(existingUser.getName())) {
                existingUser.setName(googleName);
                userRepository.save(existingUser);
                log.info("Updated Google user name for: {}", existingUser.getEmail());
            }
            return existingUser;
        } else {
            
            return linkGoogleAccount(existingUser, googleName, googleId);
            
        }
    }

    
    private User linkGoogleAccount(User existingUser, String googleName, String googleId) {
        log.info("Linking Google account to existing user: {}", existingUser.getEmail());
        
        if (!googleName.equals(existingUser.getName())) {
            existingUser.setName(googleName);
        }
        
        existingUser.setEmailVerified(true);
        existingUser.setRole(User.Role.ADMIN);
        
        existingUser.setGoogleId(googleId);
        existingUser.setProvider("google");
        
        User savedUser = userRepository.save(existingUser);
        log.info("Successfully linked Google account to user: {} and upgraded to admin role", savedUser.getEmail());
        
        return savedUser;
    }

    
    private User createGoogleUser(String email, String name, String googleId) {
        User user = User.builder()
                .email(email)
                .name(name)
                .password(passwordEncoder.encode("GOOGLE_OAUTH_" + googleId)) 
                .role(User.Role.ADMIN)
                .emailVerified(true) 
                .googleId(googleId)
                .provider("google")
                .build();
        
        return userRepository.save(user);
    }

    
    private AuthResponse generateAuthResponse(User user) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .build();

        String token = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return AuthResponse.builder()
                .user(convertToUserDto(user))
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }

    
    private com.bikash.portfolio_backend.dto.auth.UserDto convertToUserDto(User user) {
        return com.bikash.portfolio_backend.dto.auth.UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .emailVerified(user.isEmailVerified())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
} 