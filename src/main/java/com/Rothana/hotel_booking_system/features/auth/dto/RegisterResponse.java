package com.Rothana.hotel_booking_system.features.auth.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record RegisterResponse(
        String message,
        String name,
        String email,
        String phoneNumber
) {
}
