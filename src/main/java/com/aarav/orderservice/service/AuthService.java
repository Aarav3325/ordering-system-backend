package com.aarav.orderservice.service;

import com.aarav.orderservice.dto.request.LoginRequest;
import com.aarav.orderservice.dto.request.RefreshTokenRequest;
import com.aarav.orderservice.dto.request.SignUpRequest;
import com.aarav.orderservice.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(SignUpRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(String refreshToken);
}
