package com.Rothana.hotel_booking_system.features.auth;


import com.Rothana.hotel_booking_system.features.auth.dto.*;
import jakarta.mail.MessagingException;

import java.util.List;

public interface AuthService {


    RegisterResponse register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    UserResponse findById(Integer id);
    List<UserResponse> findAll();


    void  verify(VerificationRequest verificationRequest) ;
    void  sendVerification(String email) throws MessagingException;
    void resendVerification(String email) throws MessagingException;

}
