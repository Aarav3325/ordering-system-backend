package com.aarav.orderservice.service.impl;

import com.aarav.orderservice.dto.request.LoginRequest;
import com.aarav.orderservice.dto.request.SignUpRequest;
import com.aarav.orderservice.dto.response.AuthResponse;
import com.aarav.orderservice.entity.RefreshToken;
import com.aarav.orderservice.entity.Role;
import com.aarav.orderservice.entity.User;
import com.aarav.orderservice.exception.BadRequestException;
import com.aarav.orderservice.exception.UnauthorizedException;
import com.aarav.orderservice.repository.RefreshTokenRepository;
import com.aarav.orderservice.repository.UserRepository;
import com.aarav.orderservice.security.GoogleTokenVerifier;
import com.aarav.orderservice.security.JwtUtil;
import com.aarav.orderservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final GoogleTokenVerifier googleTokenVerifier;

    public AuthServiceImpl(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, GoogleTokenVerifier googleTokenVerifier) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.googleTokenVerifier = googleTokenVerifier;
    }

    @Override
    public AuthResponse register(SignUpRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        User user = new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getUsername(),
                Role.USER,
                true,
                "LOCAL"
        );

        userRepository.save(user);

        return generateToken(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new UnauthorizedException("Invalid credentials");
        }

        return generateToken(user);
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {

        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if(token.getExpiryDate().isBefore(LocalDateTime.now())){
            refreshTokenRepository.delete(token);
            throw new UnauthorizedException("Refresh token expired");
        }

        User user = token.getUser();

        String accessToken = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public AuthResponse loginWithGoogle(String idToken) {
        var payload = googleTokenVerifier.verify(idToken);

        String email = payload.getEmail();
        String name = (String) payload.get("name");

        User user = userRepository.findByEmail(email)
                .orElseGet(
                        () -> createGoogleUser(email, name)
                );

        if(!user.isEnabled()) {
            throw new RuntimeException("User is disabled");
        }

        return generateToken(user);
    }

    public AuthResponse generateToken(User user) {
        String accessToken = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        RefreshToken refreshToken =
                new RefreshToken(UUID.randomUUID().toString(),
                        LocalDateTime.now().plusDays(7),
                        user
                );

        refreshTokenRepository.save(refreshToken);

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    private User createGoogleUser(String email, String name) {
        User user = new User(
                email,
                "",
                name,
                Role.USER,
                true,
                "GOOGLE"
        );

        return userRepository.save(user);
    }
}
