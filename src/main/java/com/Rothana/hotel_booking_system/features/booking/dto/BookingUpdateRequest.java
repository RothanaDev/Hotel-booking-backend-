package com.Rothana.hotel_booking_system.features.booking.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BookingUpdateRequest(

        @NotNull(message = "Check-in date is required")
        LocalDate checkin,

        @NotNull(message = "Check-out date is required")
        LocalDate checkout,

        @Min(value = 1, message = "There must be at least 1 adult")
        int numOfAdults,

        @Min(value = 0, message = "Number of children cannot be negative")
        int numOfChildren,

        @NotNull(message = "Status is required")
        String status
) {
}
