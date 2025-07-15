package com.bikash.portfolio_backend.service;

import com.bikash.portfolio_backend.dto.auth.AuthResponse;
import com.bikash.portfolio_backend.dto.auth.LoginRequest;
import com.bikash.portfolio_backend.dto.auth.RegisterRequest;
import com.bikash.portfolio_backend.dto.auth.UpdatePersonalInfoRequest;
import com.bikash.portfolio_backend.dto.auth.UserDto;
import com.bikash.portfolio_backend.entity.User;
import com.bikash.portfolio_backend.exception.AuthenticationException;
import com.bikash.portfolio_backend.exception.ResourceNotFoundException;
import com.bikash.portfolio_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthenticationException("Email already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.USER)
                .emailVerified(false)
                .build();

        String verificationToken = generateToken();
        user.setEmailVerificationToken(verificationToken);
        user.setEmailVerificationExpiry(LocalDateTime.now().plusHours(24));

        User savedUser = userRepository.save(user);

        emailService.sendEmailVerification(savedUser.getEmail(), verificationToken);

        return generateAuthResponse(savedUser);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthenticationException("Invalid credentials"));

        return generateAuthResponse(user);
    }

    public AuthResponse refreshToken(String refreshToken) {
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AuthenticationException("Invalid refresh token"));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new AuthenticationException("Invalid refresh token");
        }

        return generateAuthResponse(user);
    }

    public void logout(String refreshToken) {
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AuthenticationException("Invalid refresh token"));

        user.setRefreshToken(null);
        userRepository.save(user);
    }

    public UserDto getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return UserDto.fromUser(user);
    }



    public UserDto updatePersonalInfo(String email, UpdatePersonalInfoRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setName(request.getName());
        
        if (request.getTitle() != null) {
            user.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            user.setDescription(request.getDescription());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getLocation() != null) {
            user.setLocation(request.getLocation());
        }
        if (request.getProfileImage() != null) {
            user.setProfileImage(request.getProfileImage());
        }
        if (request.getHeroImage() != null) {
            user.setHeroImage(request.getHeroImage());
        }

        if (request.getSocialLinks() != null) {
            user.setSocialLinks(request.getSocialLinks().stream()
                    .map(dto -> dto.toSocialLink())
                    .collect(java.util.stream.Collectors.toList()));
        }

        if (!request.getEmail().equals(email)) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new AuthenticationException("Email already exists");
            }
            user.setEmail(request.getEmail());
            user.setEmailVerified(false);
            
            String verificationToken = generateToken();
            user.setEmailVerificationToken(verificationToken);
            user.setEmailVerificationExpiry(LocalDateTime.now().plusHours(24));
            
            emailService.sendEmailVerification(request.getEmail(), verificationToken);
        }

        User updatedUser = userRepository.save(user);
        return UserDto.fromUser(updatedUser);
    }

    public void changePassword(String email, String currentPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new AuthenticationException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String resetToken = generateToken();
        user.setPasswordResetToken(resetToken);
        user.setPasswordResetExpiry(LocalDateTime.now().plusHours(1));

        userRepository.save(user);
        emailService.sendPasswordResetEmail(email, resetToken);
    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByPasswordResetTokenAndExpiryAfter(token, LocalDateTime.now())
                .orElseThrow(() -> new AuthenticationException("Invalid or expired reset token"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetExpiry(null);

        userRepository.save(user);
    }

    public void verifyEmail(String token) {
        User user = userRepository.findByEmailVerificationTokenAndExpiryAfter(token, LocalDateTime.now())
                .orElseThrow(() -> new AuthenticationException("Invalid or expired verification token"));

        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        user.setEmailVerificationExpiry(null);

        userRepository.save(user);
    }

    private AuthResponse generateAuthResponse(User user) {
        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return AuthResponse.builder()
                .user(UserDto.fromUser(user))
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }

    private String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
} 