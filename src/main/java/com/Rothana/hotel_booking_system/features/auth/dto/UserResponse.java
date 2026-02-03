package com.Rothana.hotel_booking_system.features.auth.dto;

public record UserResponse(
        Integer id,
        String name,
        String email,
        String phoneNumber
) {
}
