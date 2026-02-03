package com.Rothana.hotel_booking_system.features.auth.dto;

import lombok.Builder;

@Builder
public record AuthResponse(
        //Token type
        String tokenType,

        String accessToken,

        String refreshToken
) {
}
