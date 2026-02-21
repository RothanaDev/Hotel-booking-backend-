package com.Rothana.hotel_booking_system.features.booking.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record BookingCreateRequest(
        @NotNull(message = "User ID is required")
        Integer userId,

        @NotNull(message = "Room ID is required")
        Integer roomId,

        @NotNull(message = "Check-in date is required")
        LocalDate checkin,

        @NotNull(message = "Check-out date is required")
        LocalDate checkout,

        @Min(value = 1, message = "There must be at least 1 adult")
        int numOfAdults,

        @Min(value = 0, message = "Number of children cannot be negative")
        int numOfChildren,

        @Valid
        List<ServiceItemRequest> services
) {
    public record ServiceItemRequest(
            @NotNull(message = "Service ID is required")
            Integer serviceId,

            @Min(value = 1, message = "Quantity must be at least 1")
            Integer quantity
    ) {}
}
