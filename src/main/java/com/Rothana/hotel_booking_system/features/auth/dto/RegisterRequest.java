package com.Rothana.hotel_booking_system.features.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Phone number is required")
        @Size( max = 10, min = 9, message = "Phone number must be between 9 to 10 digits ")
        String phoneNumber,

        @NotBlank(message = "Email is required")
        String email,
        @NotBlank(message = " Password is required")
        @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
                message ="Password must be contain minimum 8 character in length" +
                        "At least one uppercase English letter" +
                        "At least one English lowercase English Letter" +
                        "AT least one digit" +
                        "At least one special character") // Regular Expression
        String password
) {
}
