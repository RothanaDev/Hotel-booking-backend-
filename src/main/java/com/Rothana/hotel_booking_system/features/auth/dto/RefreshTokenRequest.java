package com.Rothana.hotel_booking_system.features.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank(message = "Refresh Token is Required")
        String refreshToken
) {
}
