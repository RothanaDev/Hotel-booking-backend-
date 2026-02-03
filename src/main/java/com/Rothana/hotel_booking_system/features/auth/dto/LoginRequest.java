package com.Rothana.hotel_booking_system.features.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;


public record LoginRequest(
        @NotBlank(message = "Email is Required")
        String email,
        @NotBlank(message = "Password Number is Required")
        String password
) {
}
