package com.Rothana.hotel_booking_system.features.auth;


import com.Rothana.hotel_booking_system.features.auth.dto.*;

import java.util.List;

public interface AuthService {


    RegisterResponse register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    UserResponse findById(Integer id);
    List<UserResponse> findAll();
}
