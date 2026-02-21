package com.Rothana.hotel_booking_system.features.auth.dto;

public record MeResponse(
        Integer id,
        String email,
        String role
) {}